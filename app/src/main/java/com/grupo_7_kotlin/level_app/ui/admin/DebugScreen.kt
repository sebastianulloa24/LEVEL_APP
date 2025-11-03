package com.grupo_7_kotlin.level_app.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugScreen(
    usuarioViewModel: UsuarioViewModel,
    onNavigateToLogin: () -> Unit, // Navega a Login después del borrado
    onNavigateBack: () -> Unit     // Vuelve a la pantalla anterior
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Herramientas de Administración") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "¡ADVERTENCIA! Estas acciones borrarán datos de la base de datos local y no se pueden deshacer.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // BOTÓN DE RESETEO MOVIDO A UN LUGAR SEGURO
            Button(
                onClick = {
                    usuarioViewModel.resetAllData() // Borra todos los usuarios y datos
                    onNavigateToLogin()             // Fuerza el cierre de sesión y va al login
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("PELIGRO: BORRAR TODOS LOS USUARIOS Y DATOS")
            }
        }
    }
}