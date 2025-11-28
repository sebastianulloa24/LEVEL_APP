package com.grupo_7_kotlin.level_app.test.login

import com.grupo_7_kotlin.level_app.data.dao.UsuarioDao
import com.grupo_7_kotlin.level_app.data.model.Usuario
import com.grupo_7_kotlin.level_app.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val usuarioDao = mockk<UsuarioDao>()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(usuarioDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // -----------------------------
    // LOGIN EXITOSO
    // -----------------------------
    @Test
    fun loginExitosoActualizaUiStateSinError() = runTest {
        // Given
        val email = "sebastian@test.com"
        val password = "1234"
        val passwordHash = password.hashCode().toString()

        val usuarioFake = Usuario(
            id = 1,
            email = email,
            passwordHash = passwordHash,
            birthDate = 0L,
            hasDuocDiscount = false,
            levelUpPoints = 0,
            myReferralCode = "ABC123"
        )

        coEvery { usuarioDao.obtenerUsuarioPorEmail(email) } returns usuarioFake

        // When
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.submit {}

        advanceUntilIdle()

        // Then
        assert(viewModel.uiState.error == null)
        assert(viewModel.uiState.isLoading == false)
    }


    // -----------------------------
    // LOGIN FALLIDO
    // -----------------------------
    @Test
    fun loginFallidoMuestraErrorDeCredenciales() = runTest {
        val email = "test@test.com"
        val password = "wrong"

        coEvery { usuarioDao.obtenerUsuarioPorEmail(email) } returns null

        // When
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.submit {}

        advanceUntilIdle()

        // Then
        assert(viewModel.uiState.error == "Credenciales Inválidas")
    }

    // -----------------------------
    // LOGIN EXCEPCIÓN
    // -----------------------------
    @Test
    fun SiUsuarioDaoLanzaExcepcionSeMuestraErrorDeConexion() = runTest {
        val email = "error@test.com"

        coEvery { usuarioDao.obtenerUsuarioPorEmail(email) } throws Exception("DB DOWN")

        // When
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange("whatever")
        viewModel.submit {}

        advanceUntilIdle()

        // Then
        assert(viewModel.uiState.error == "Error de conexión o sistema.")
    }

    // -----------------------------
    // CAMBIO DE EMAIL
    // -----------------------------
    @Test
    fun onEmailChangeactualizaelemailenuiState() {
        viewModel.onEmailChange("nuevo@test.com")

        assert(viewModel.uiState.email == "nuevo@test.com")
    }

    // -----------------------------
    // CAMBIO DE PASSWORD
    // -----------------------------
    @Test
    fun onPasswordChangeactualizaelpasswordenuiState() {
        viewModel.onPasswordChange("1234")

        assert(viewModel.uiState.password == "1234")
    }
}