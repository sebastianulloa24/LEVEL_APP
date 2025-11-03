package com.grupo_7_kotlin.level_app.ui.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilScreen(
    viewModel: UsuarioViewModel,
    onLogoutSuccess: () -> Unit, // Función de callback para cerrar sesión y navegar al login (NOTA: Se corrigió la coma aquí)
    onNavigateToCanje: () -> Unit, // Función de callback para navegar a la pantalla de canje de puntos
    onNavigateToDebugScreen: () -> Unit
) {
    // 1. Observar el estado del usuario logueado desde el ViewModel compartido
    val usuario by viewModel.currentUser.collectAsState()

    // Formateador para mostrar la fecha de nacimiento en el formato deseado
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil Level-Up") },
                actions = {
                    // Botón de Cerrar Sesión ubicado en la barra superior
                    IconButton(onClick = onLogoutSuccess) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 2. Bloque condicional: Mostrar el contenido de perfil SOLO SI el usuario existe (está logueado)
            usuario?.let { usuarioActual ->

                // --- Tarjeta de Puntos y Bienvenida ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Muestra el email del usuario quitando la parte del dominio
                        Text(
                            text = "Bienvenido, ${usuarioActual.email.substringBefore('@')}!",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(Modifier.height(16.dp))
                        // Muestra la cantidad actual de puntos Level-Up
                        Text(
                            text = "Puntos Level-Up: ${usuarioActual.levelUpPoints}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                // --- Detalles de la Cuenta (Filas de Datos) ---
                DatosFila(
                    label = "Email de Cuenta",
                    value = usuarioActual.email
                )
                DatosFila(
                    label = "Fecha Nacimiento",
                    value = dateFormatter.format(Date(usuarioActual.birthDate))
                )
                DatosFila(
                    label = "Código Referencial",
                    value = usuarioActual.myReferralCode
                )
                DatosFila(
                    label = "Descuento Duoc",
                    value = if (usuarioActual.hasDuocDiscount) "Aplicado (20%)" else "No Aplicado"
                )

                Spacer(Modifier.height(32.dp))

                // --- Botón de Navegación de Canje ---
                Button(
                    onClick = onNavigateToCanje,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Canjear Puntos y Descuentos")
                }

                Spacer(Modifier.height(16.dp))


            } ?: run {
                // 3. Fallback: Se muestra si el usuario es nulo (sesión no iniciada o caducada)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Error: Sesión caducada.",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onLogoutSuccess) {
                        Text("Volver a Iniciar Sesión")
                    }
                }
            }
            Spacer(Modifier.height(32.dp))

            // ENLACE DISCRETO A LA PANTALLA DE DEBUG (Al final de la pantalla)
            /*TextButton(
                onClick = onNavigateToDebugScreen, // Llama a la nueva navegación
                modifier = Modifier.align(Alignment.End) // Alineado a la derecha para que se vea como un link
            ) {
                Text(
                    "Opciones de Administrador",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }*/
        }
    }
}

// Componente reusable para mostrar una fila de datos de perfil
@Composable
fun DatosFila(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label + ":",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.secondary
        )
    }
    Divider(Modifier.fillMaxWidth())
}