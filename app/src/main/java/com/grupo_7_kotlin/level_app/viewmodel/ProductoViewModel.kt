package com.grupo_7_kotlin.level_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo_7_kotlin.level_app.data.dao.ProductoDao
import com.grupo_7_kotlin.level_app.data.dao.ResenaDao
import com.grupo_7_kotlin.level_app.data.model.Producto
import com.grupo_7_kotlin.level_app.data.model.Resena
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class ProductoViewModel(
    private val productoDao: ProductoDao,
    private val resenaDao: ResenaDao
) : ViewModel() {
    // --- ESTADOS DE BÚSQUEDA Y FILTRADO (Catálogo) ---
    private val _consultaBusqueda = MutableStateFlow("")
    private val _categoriaSeleccionada =
        MutableStateFlow<String?>(null)
    val consultaBusqueda: StateFlow<String> get() = _consultaBusqueda
    val categoriaSeleccionada: StateFlow<String?> get() = _categoriaSeleccionada

    val categorias = listOf(
        "Juegos de Mesa", "Accesorios", "Consolas", "Computadores Gamers", "Sillas Gamers",
        "Mouse", "Mousepad", "Poleras Personalizadas", "Servicio Técnico"
    )

    // Flow que combina los filtros y los productos del DAO para generar la lista filtrada
    val productosFiltrados: StateFlow<List<Producto>> =
        combine(
            _consultaBusqueda,
            _categoriaSeleccionada,
            productoDao.obtenerTodosLosProductosFlow()
        ) { consulta, categoria, todosLosProductos ->
            // 1. Filtrar por categoría
            var listaFiltrada = if (categoria != null) {
                todosLosProductos.filter { it.categoria == categoria }
            } else {
                todosLosProductos
            }

            // 2. Filtrar por texto de búsqueda
            if (consulta.isNotBlank()) {
                listaFiltrada = listaFiltrada.filter {
                    it.nombre.contains(consulta, ignoreCase = true) ||
                            it.descripcion.contains(consulta, ignoreCase = true)
                }
            }
            listaFiltrada
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // --- ESTADO Y LÓGICA PARA EL DETALLE DEL PRODUCTO Y RESEÑAS ---

    private val _productoDetalle =
        MutableStateFlow<Producto?>(null)
    val productoDetalle: StateFlow<Producto?> = _productoDetalle.asStateFlow()

    private val _resenasProducto =
        MutableStateFlow<List<Resena>>(emptyList())
    val resenasProducto: StateFlow<List<Resena>> = _resenasProducto.asStateFlow()

    //Carga el producto de detalle y sus reseñas basándose en el ID alfanumérico.
    // @param productIdString ID del producto (Ej: "JM001")
    fun loadProductoDetalle(productIdString: String) {
        if (productIdString.isNotBlank()) {
            viewModelScope.launch {
                try {
                    // 1. Cargar el producto usando el ID (String)
                    val productoEncontrado = productoDao.obtenerProductoPorId(productIdString)
                    _productoDetalle.value = productoEncontrado

                    // 2. Cargar las reseñas usando Flow.
                    resenaDao.obtenerResenasPorProducto(productIdString).collectLatest { reviews ->
                        _resenasProducto.value = reviews
                    }
                } catch (e: Exception) {
                    _productoDetalle.value = null
                    _resenasProducto.value = emptyList()
                }
            }
        } else {
            _productoDetalle.value = null
            _resenasProducto.value = emptyList()
        }
    }

    //Inserta una nueva reseña en la base de datos.

    fun crearResena(
        productoId: String,
        userId: Int,
        calificacion: Int,
        comentario: String
    ) {
        viewModelScope.launch {
            val nuevaResena = Resena( // Crea el objeto Resena
                productoId = productoId,
                usuarioId = userId, // CORREGIDO: Usa el ID del usuario aquí
                calificacion = calificacion,
                comentario = comentario
            )
            resenaDao.insertarResena(nuevaResena) // Llama al DAO para insertar
        }
    }

    // --- FUNCIONES DE INTERACCIÓN DE UI ---

    fun actualizarConsultaBusqueda(consulta: String) {
        _consultaBusqueda.value = consulta
    }

    fun seleccionarCategoria(categoria: String?) {
        _categoriaSeleccionada.value = categoria
    }
}