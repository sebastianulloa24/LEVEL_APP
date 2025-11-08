package com.grupo_7_kotlin.level_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCanje(
    modifier: Modifier = Modifier, // <- aceptamos modifier externo
    navController: NavController,
    usuarioViewModel: UsuarioViewModel
) {
    // Observa al usuario (StateFlow en el ViewModel)
    val usuario by usuarioViewModel.currentUser.collectAsState(initial = null)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val opcionesCanje = listOf(
        CanjeItem(100, "10% de Descuento en la próxima compra"),
        CanjeItem(500, "Producto Sorpresa (Envío Gratis)"),
        CanjeItem(1500, "30% de Descuento en toda la web")
    )

    Scaffold(
        modifier = modifier, // aplicamos el modifier recibido
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Canjea tus Puntos Level-Up") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tus Puntos Actuales: ${usuario?.levelUpPoints ?: 0} ⭐",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            opcionesCanje.forEach { item ->
                CanjeCard(
                    item = item,
                    puntosDisponibles = usuario?.levelUpPoints ?: 0,
                    onCanjeClick = { puntos ->
                        // Llamada al ViewModel para canjear; retorna booleano si fue exitoso
                        val exito = usuarioViewModel.canjearPuntosPorDescuento(puntos)

                        scope.launch {
                            if (exito) {
                                snackbarHostState.showSnackbar("¡Canje Exitoso! Disfruta tu descuento.")
                            } else {
                                snackbarHostState.showSnackbar("Puntos insuficientes o error de canje.")
                            }
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

data class CanjeItem(val puntos: Int, val descripcion: String)

// CanjeCard (usa Material3 y no pasa enabled al Card)
@Composable
fun CanjeCard(item: CanjeItem, puntosDisponibles: Int, onCanjeClick: (Int) -> Unit) {
    val isEnabled = puntosDisponibles >= item.puntos

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.descripcion, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text("${item.puntos} Puntos", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }

            Button(onClick = { onCanjeClick(item.puntos) }, enabled = isEnabled) {
                Text("Canjear")
            }
        }
    }
}
