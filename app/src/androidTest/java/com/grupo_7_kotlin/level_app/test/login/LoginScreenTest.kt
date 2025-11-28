package com.grupo_7_kotlin.level_app.test.login

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.grupo_7_kotlin.level_app.data.database.AppDatabase
import com.grupo_7_kotlin.level_app.data.model.Usuario
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel
import com.grupo_7_kotlin.level_app.ui.login.LoginScreen
import kotlinx.coroutines.runBlocking
import org.junit.*

class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun login_exitoso_navega_correctamente() = runBlocking {

        val email = "sebastian@test.com"
        val password = "1234"
        val passwordHash = password.hashCode().toString()

        db.usuarioDao().insertarUsuario(
            Usuario(
                email = email,
                passwordHash = passwordHash,
                birthDate = 0L,
                hasDuocDiscount = false,
                levelUpPoints = 0,
                myReferralCode = "ABC123"
            )
        )

        val viewModel = UsuarioViewModel(db.usuarioDao())

        var navego = false

        composeRule.setContent {
            MaterialTheme {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = { navego = true },
                    onGoToRegister = {}
                )
            }
        }

        composeRule.onNodeWithText("Email").performTextInput(email)
        composeRule.onNodeWithText("Contraseña").performTextInput(password)
        composeRule.onNodeWithText("Iniciar Sesión").performClick()

        composeRule.waitUntil(timeoutMillis = 3000) {
            navego
        }

        assert(navego)
    }

    @Test
    fun login_fallido_muestra_mensaje_error() {

        val viewModel = UsuarioViewModel(db.usuarioDao())

        composeRule.setContent {
            MaterialTheme {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {},
                    onGoToRegister = {}
                )
            }
        }

        composeRule.onNodeWithText("Email").performTextInput("correo@invalido")
        composeRule.onNodeWithText("Contraseña").performTextInput("1234")
        composeRule.onNodeWithText("Iniciar Sesión").performClick()

        composeRule.waitForIdle()

        composeRule
            .onNodeWithText("Error: Usuario no encontrado")
            .assertIsDisplayed()
    }
}
