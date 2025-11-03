package com.grupo_7_kotlin.level_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo_7_kotlin.level_app.data.dao.UsuarioDao
import com.grupo_7_kotlin.level_app.data.model.Usuario
import com.grupo_7_kotlin.level_app.data.repository.ResultadoAutenticacion
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class UsuarioViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {

    private val _registrationResult = MutableStateFlow<AuthResult?>(null)
    val registrationResult: StateFlow<AuthResult?> = _registrationResult.asStateFlow()

    private val _loginResult = MutableStateFlow<ResultadoAutenticacion?>(null)
    val loginResult: StateFlow<ResultadoAutenticacion?> = _loginResult.asStateFlow()

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _descuento = MutableStateFlow(0)
    val descuento: StateFlow<Int> = _descuento.asStateFlow()

    val currentLevel: StateFlow<Int> = currentUser
        .map { usuario -> usuario?.levelUpPoints?.let { getLevelFromPoints(it) } ?: 1 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    // --- LOGIN ---
    fun iniciarSesion(email: String, password: String) {
        viewModelScope.launch {
            try {
                val usuario = usuarioDao.obtenerUsuarioPorEmail(email)
                val passwordHashIngresado = password.hashCode().toString()
                if (usuario != null && usuario.passwordHash == passwordHashIngresado) {
                    _loginResult.value = ResultadoAutenticacion.Exito
                    _currentUser.value = usuario
                } else {
                    _loginResult.value = ResultadoAutenticacion.Error("Email o contraseña incorrectos.")
                    _currentUser.value = null
                }
            } catch (e: Exception) {
                _loginResult.value = ResultadoAutenticacion.Error("Error de sistema al iniciar sesión.")
                _currentUser.value = null
            }
        }
    }

    // --- REGISTRO ---
    fun registerUser(
        email: String,
        password: String,
        birthDateString: String,
        referralCode: String
    ) {
        viewModelScope.launch {
            val isOver18 = try {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val birthDate = sdf.parse(birthDateString) ?: Date()
                val today = Calendar.getInstance()
                today.add(Calendar.YEAR, -18)
                birthDate.before(today.time)
            } catch (e: Exception) {
                _registrationResult.value = AuthResult.Error("Formato de fecha inválido. Usa dd/MM/yyyy.")
                return@launch
            }

            if (!isOver18) {
                _registrationResult.value = AuthResult.Error("Debes ser mayor de 18 años para registrarte.")
                return@launch
            }

            val hasDuocDiscount = email.endsWith("@duocuc.cl", true) || email.endsWith("@profesor.duoc.cl", true)
            val passwordHash = password.hashCode().toString()
            val myNewReferralCode = "LVL-${(1000..9999).random()}"

            var newUser = Usuario(
                email = email,
                passwordHash = passwordHash,
                birthDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(birthDateString)!!.time,
                hasDuocDiscount = hasDuocDiscount,
                levelUpPoints = 0,
                myReferralCode = myNewReferralCode
            )

            if (referralCode.isNotBlank()) {
                val referrer = usuarioDao.getUserByReferralCode(referralCode)
                if (referrer != null) {
                    val updatedReferrer = referrer.copy(levelUpPoints = referrer.levelUpPoints + 100)
                    usuarioDao.updateUser(updatedReferrer)
                    newUser = newUser.copy(levelUpPoints = 50)
                }
            }

            usuarioDao.insertarUsuario(newUser)
            _currentUser.value = usuarioDao.obtenerUsuarioPorEmail(email)
            _registrationResult.value = AuthResult.Success
        }
    }

    // --- DESCUENTOS ---
    fun aplicarDescuento(valor: Int) {
        val usuarioActual = _currentUser.value ?: return
        if (valor <= 0) return
        _descuento.value = valor

        viewModelScope.launch {
            try {
                val usuarioActualizado = usuarioActual.copy(hasDuocDiscount = true)
                usuarioDao.updateUser(usuarioActualizado)
                _currentUser.value = usuarioActualizado
            } catch (e: Exception) {
                Log.e("UsuarioVM", "Error al aplicar descuento: ${e.message}")
            }
        }
    }

    fun canjearPuntosPorDescuento(puntosRequeridos: Int): Boolean {
        val usuarioActual = _currentUser.value ?: return false
        if (usuarioActual.levelUpPoints >= puntosRequeridos) {
            val nuevosPuntos = usuarioActual.levelUpPoints - puntosRequeridos
            val usuarioActualizado = usuarioActual.copy(levelUpPoints = nuevosPuntos)
            viewModelScope.launch { usuarioDao.updateUser(usuarioActualizado) }
            _currentUser.value = usuarioActualizado
            return true
        }
        return false
    }

    // --- UTILIDADES ---
    fun logout() { _currentUser.value = null; _loginResult.value = null }

    fun resetAllData() {
        viewModelScope.launch {
            _currentUser.value = null
            usuarioDao.deleteAllUsers()
        }
    }

    fun clearLoginResult() { _loginResult.value = null }
    fun clearRegistrationResult() { _registrationResult.value = null }

    fun getLevelFromPoints(points: Int): Int {
        return when {
            points >= 3000 -> 5
            points >= 1500 -> 4
            points >= 500 -> 3
            points >= 100 -> 2
            else -> 1
        }
    }

    fun actualizarEmail(nuevoEmail: String) {
        val usuarioActual = _currentUser.value ?: return
        val usuarioActualizado = usuarioActual.copy(email = nuevoEmail)
        viewModelScope.launch {
            try {
                usuarioDao.updateUser(usuarioActualizado)
                _currentUser.value = usuarioActualizado
            } catch (e: Exception) {
                Log.e("UsuarioVM", "Error al actualizar email: ${e.message}")
            }
        }
    }
}
