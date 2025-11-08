package com.grupo_7_kotlin.level_app.ui.catalogo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo_7_kotlin.level_app.viewmodel.ProductoViewModel
import com.grupo_7_kotlin.level_app.viewmodel.ProductoViewModelFactory
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import com.grupo_7_kotlin.level_app.data.model.Resena
import com.grupo_7_kotlin.level_app.ui.components.RatingBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleProducto(
    productId: String,
    usuarioViewModel: UsuarioViewModel,
    onNavigateBack: () -> Unit
) {
    // 1. Instanciar ViewModels
    val productoViewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(LocalContext.current)
    )

    // 2. Observar estados
    LaunchedEffect(productId) {
        productoViewModel.loadProductoDetalle(productId)
    }

    val producto by productoViewModel.productoDetalle.collectAsState()
    val resenas by productoViewModel.resenasProducto.collectAsState()
    val currentUser by usuarioViewModel.currentUser.collectAsState()

    // Obtenemos la ID del usuario logueado para enviarla a la reseña
    val currentUserId = currentUser?.id ?: 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección de Detalle del Producto
            item {
                producto?.let {
                    Text(it.nombre, style = MaterialTheme.typography.headlineLarge)
                    Spacer(Modifier.height(8.dp))
                    Text(it.descripcion, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(16.dp))
                    Text("Precio: $${it.precio}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                } ?: run {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Text("Buscando producto (ID: $productId)...")
                    }
                }
            }
            // Sección de Formulario de Reseña
            item {
                currentUser?.let {
                    FormularioResena(
                        productId = productId,
                        userId = currentUserId,
                        viewModel = productoViewModel
                    )
                } ?: Text("Inicia sesión para dejar una reseña.", style = MaterialTheme.typography.titleMedium)
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
            // Sección de Lista de Reseñas
            item {
                Text("Reseñas de Clientes (${resenas.size})", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                if (resenas.isEmpty()) {
                    Text("Aún no hay reseñas para este producto.", style = MaterialTheme.typography.bodyMedium)
                }
            }
            // Lista de Reseñas
            items(resenas) { resena ->
                ResenaCard(resena = resena)
            }
        }
    }
}
// --- COMPONENTES AUXILIARES ---

@Composable
fun FormularioResena(
    productId: String,
    userId: Int,
    viewModel: ProductoViewModel
) {
    var calificacion by remember { mutableStateOf(5) }
    var comentario by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text("Deja tu Reseña", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        // 1. Calificación
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tu calificación:", modifier = Modifier.width(120.dp))

            // Usando RatingBar
            // Asegúrate de que RatingBar.kt esté creado
            RatingBar(
                rating = calificacion.toFloat(),
                onRatingChange = { calificacion = it.toInt() }
            )
        }

        // 2. Comentario
        OutlinedTextField(
            value = comentario,
            onValueChange = { comentario = it },
            label = { Text("Tu opinión (Opcional)") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp)
        )

        Spacer(Modifier.height(16.dp))

        // 3. Botón de Enviar
        Button(
            onClick = {
                viewModel.crearResena(productId, userId, calificacion, comentario.trim())
                calificacion = 5
                comentario = ""
            },
            enabled = comentario.isNotBlank() || calificacion > 0
        ) {
            Text("Enviar Reseña")
        }
    }
}

@Composable
fun ResenaCard(resena: Resena) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Estrellas de Calificación
            Row(verticalAlignment = Alignment.CenterVertically) {
                (1..5).forEach { star ->
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "$star estrellas",
                        tint = if (star <= resena.calificacion) Color(0xFFFFC107) else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "(${resena.calificacion} / 5)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.height(4.dp))

            // Usuario y Fecha
            Text(
                "Por Usuario ID: ${resena.usuarioId} - Hace poco",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))

            // Comentario
            if (resena.comentario.isNotBlank()) {
                Text(resena.comentario, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}