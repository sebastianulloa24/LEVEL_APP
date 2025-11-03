package com.grupo_7_kotlin.level_app.ui.catalogo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grupo_7_kotlin.level_app.data.model.Producto
import com.grupo_7_kotlin.level_app.viewmodel.ProductoViewModel
import com.grupo_7_kotlin.level_app.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(
    modifier: Modifier = Modifier,
    productoViewModel: ProductoViewModel,
    usuarioViewModel: UsuarioViewModel,
    onProductoClick: (productId: String) -> Unit,
    onProfileClick: () -> Unit,
    onScanQrClick: () -> Unit
) {
    // --- Estados reactivos ---
    val consultaBusqueda by productoViewModel.consultaBusqueda.collectAsState(initial = "")
    val categoriaSeleccionada by productoViewModel.categoriaSeleccionada.collectAsState(initial = null)
    val productos by productoViewModel.productosFiltrados.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo Level-Up Gamer") },
                actions = {
                    IconButton(onClick = onScanQrClick) {
                        Icon(Icons.Filled.QrCodeScanner, contentDescription = "Escanear Código QR")
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {

            // 1. Buscador
            OutlinedTextField(
                value = consultaBusqueda,
                onValueChange = { productoViewModel.actualizarConsultaBusqueda(it) },
                label = { Text("Buscar productos (nombre o descripción)...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                singleLine = true
            )

            // 2. Filtro de Categorías
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = categoriaSeleccionada == null,
                        onClick = { productoViewModel.seleccionarCategoria(null) },
                        label = { Text("Todos") }
                    )
                }
                items(productoViewModel.categorias) { category ->
                    FilterChip(
                        selected = categoriaSeleccionada == category,
                        onClick = { productoViewModel.seleccionarCategoria(category) },
                        label = { Text(category) }
                    )
                }
            }

            // 3. Lista de Productos Filtrados
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productos) { producto ->
                    ProductoCard(
                        producto = producto,
                        onClick = { onProductoClick(producto.id.toString()) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    producto.categoria,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text("$${String.format("%,.0f", producto.precio)}", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}
