package com.grupo_7_kotlin.level_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo_7_kotlin.level_app.data.dao.UsuarioDao
import com.grupo_7_kotlin.level_app.ui.login.LoginUiState
import kotlinx.coroutines.launch

class LoginViewModel (
    private val usuarioDao: UsuarioDao
): ViewModel(){


    var uiState by mutableStateOf(LoginUiState())

    fun onEmailChange(value:String){
        uiState=uiState.copy(email = value, error = null)
    }

    fun onPasswordChange(value:String){
        uiState=uiState.copy(password = value, error = null)
    }

    fun submit(onSuccess: (String) -> Unit){
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            try {
                // Lógica de autenticación...
                val usuario = usuarioDao.obtenerUsuarioPorEmail(uiState.email.trim())
                val passwordHashIngresado = uiState.password.hashCode().toString()

                if (usuario != null && usuario.passwordHash == passwordHashIngresado) {
                    uiState = uiState.copy(isLoading = false, error = null)
                    onSuccess(uiState.email.trim())
                } else {
                    uiState = uiState.copy(isLoading = false, error = "Credenciales Inválidas")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = "Error de conexión o sistema.")
            }
        }
    }
}