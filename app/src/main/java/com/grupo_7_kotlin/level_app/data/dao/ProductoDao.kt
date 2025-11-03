package com.grupo_7_kotlin.level_app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grupo_7_kotlin.level_app.data.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun obtenerTodosLosProductosFlow(): Flow<List<Producto>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodosLosProductos(productos: List<Producto>)
    @Query("SELECT * FROM productos WHERE id = :productId LIMIT 1")
    suspend fun obtenerProductoPorId(productId: String): Producto?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: Producto)
}