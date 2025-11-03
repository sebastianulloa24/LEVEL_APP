package com.grupo_7_kotlin.level_app.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo_7_kotlin.level_app.viewmodel.AuthResult
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModelFactory

@Composable
fun RegisterScreen(
    viewModel: UsuarioViewModel = viewModel(factory = UsuarioViewModelFactory(LocalContext.current)),
    // CORRECCIÓN: onRegisterSuccess debe ser una función normal (no @Composable)
    onRegisterSuccess: () -> Unit,
    onShowLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var referralCode by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val registrationResult by viewModel.registrationResult.collectAsState()

    LaunchedEffect(registrationResult) {
        when (val result = registrationResult) {
            is AuthResult.Success -> {
                // NOTA: Es mejor mostrar el Snackbar después de la navegación o en la pantalla de catálogo.
                snackbarHostState.showSnackbar("✅ ¡Registro exitoso! ¡Bienvenido a Level-Up!")

                // Esta llamada ahora es válida
                onRegisterSuccess()

                // Limpia el estado después de la navegación para evitar re-ejecución
                viewModel.clearRegistrationResult()
            }
            is AuthResult.Error -> {
                snackbarHostState.showSnackbar("❌ Error: ${result.message}")
                viewModel.clearRegistrationResult()
            }
            null -> Unit
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Registro Level-Up", style = MaterialTheme.typography.headlineMedium)
            Text("¡Descuento 20% si eres Duoc! 🎓", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(16.dp))

            // Campos de entrada
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            // ... (el resto de tus campos de texto) ...
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("passwordHash") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Fecha de Nacimiento (dd/MM/yyyy)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = referralCode,
                onValueChange = { referralCode = it },
                label = { Text("Código de Referido (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.registerUser(email, password, birthDate, referralCode)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            TextButton(onClick = onShowLogin) {
                Text("¿Ya tienes cuenta? Inicia Sesión")
            }
        }
    }
}