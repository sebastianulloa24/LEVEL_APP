package com.grupo_7_kotlin.level_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
// Importes del ViewModel
import com.grupo_7_kotlin.level_app.data.repository.ResultadoAutenticacion
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel


@Composable
fun LoginScreen(
    viewModel: UsuarioViewModel,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    val loginResult by viewModel.loginResult.collectAsState()

    // 1. Escuchar el resultado y reaccionar (navegación o error)
    LaunchedEffect(loginResult) {
        when (val result = loginResult) {
            is ResultadoAutenticacion.Exito -> {
                // El Snackbar y la navegación deben ser lo último
                snackbarHostState.showSnackbar("✅ ¡Bienvenido! Iniciando sesión...")

                // NOTA: La función iniciarSesion ya actualizó _currentUser.value = usuario.
                // Como es una StateFlow, el cambio se refleja de inmediato.

                onLoginSuccess() // Esto dispara la navegación a CATALOG_SCREEN
                viewModel.clearLoginResult()
            }
            is ResultadoAutenticacion.Error -> {
                snackbarHostState.showSnackbar("❌ Error: ${result.mensaje}")
                viewModel.clearLoginResult()
            }
            null -> Unit
        }
    }

    // --- ESTA PARTE ES CRUCIAL SI QUIERES VERIFICAR QUE EL USUARIO EXISTE ---
    // Aunque no es estrictamente necesario, es una doble verificación
    /*
    val currentUser by viewModel.currentUser.collectAsState()
    LaunchedEffect(currentUser) {
        if (currentUser != null && loginResult is ResultadoAutenticacion.Exito) {
             // Ya tenemos el usuario y el login fue exitoso, ahora podemos navegar
             onLoginSuccess()
             viewModel.clearLoginResult()
        }
    }
    */

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Iniciar Sesión Level-Up", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(32.dp))

            // Campo de Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de passwordHash
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") }, // Mejorar el label
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // Botón de Login
            Button(
                onClick = {
                    // Llama a la función del ViewModel
                    viewModel.iniciarSesion(email, password)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotBlank() && password.isNotBlank()
            ) {
                Text("Iniciar Sesión")
            }

            Spacer(Modifier.height(16.dp))

            // Botón para ir a Registro
            TextButton(onClick = onGoToRegister) {
                Text("¿No tienes cuenta? Regístrate aquí.")
            }
        }
    }
}