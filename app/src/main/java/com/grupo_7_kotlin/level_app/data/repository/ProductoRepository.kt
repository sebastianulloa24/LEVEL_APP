package com.grupo_7_kotlin.level_app.data.repository

import com.grupo_7_kotlin.level_app.data.dao.ProductoDao
import com.grupo_7_kotlin.level_app.data.model.Producto
import kotlinx.coroutines.flow.Flow


class ProductoRepository(private val productoDao: ProductoDao) {

    suspend fun insertarProducto(producto: Producto) {
        productoDao.insertarProducto(producto)
    }

    fun obtenerProductos(): Flow<List<Producto>> {
        return productoDao.obtenerTodosLosProductosFlow()
    }
}
