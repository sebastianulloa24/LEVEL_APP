package com.grupo_7_kotlin.level_app.ui.perfil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.collectAsState // Para .collectAsState()
import androidx.compose.runtime.getValue        // Para el delegado 'by'
import androidx.compose.runtime.Composable     // Para que Compose funcione
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModelFactory


@Composable
fun ProfilScreen(
    viewModel: UsuarioViewModel = viewModel(factory = UsuarioViewModelFactory(LocalContext.current)),
    onLogout: () -> Unit
) {
    val usuario by viewModel.currentUser.collectAsState()

    usuario?.let { usuarioActual ->
        Column(modifier = Modifier.padding(16.dp)) {
            // ... (Contenido de la pantalla) ...

            // Botón de Cierre de Sesión (usando onLogout)
            Button(
                onClick = {
                    viewModel.logout() // Llama a la lógica de limpieza
                    onLogout() // Llama a la función de navegación (popBackStack/navigate)
                },
                // ...
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}