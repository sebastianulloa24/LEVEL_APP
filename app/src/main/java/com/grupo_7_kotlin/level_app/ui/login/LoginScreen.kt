package com.grupo_7_kotlin.level_app.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.grupo_7_kotlin.level_app.data.repository.ResultadoAutenticacion
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: UsuarioViewModel,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val loginResult by viewModel.loginResult.collectAsState()

    LaunchedEffect(loginResult) {
        when (val result = loginResult) {
            is ResultadoAutenticacion.Exito -> {
                snackbarHostState.showSnackbar("Login exitoso")
                onLoginSuccess()
                viewModel.clearLoginResult()
            }
            is ResultadoAutenticacion.Error -> {
                snackbarHostState.showSnackbar("Error: ${result.mensaje}")
                viewModel.clearLoginResult()
            }
            null -> Unit
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { viewModel.iniciarSesion(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
        }
    }
}
