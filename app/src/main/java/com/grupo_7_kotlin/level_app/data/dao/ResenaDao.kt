package com.grupo_7_kotlin.level_app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.grupo_7_kotlin.level_app.data.model.Resena
import kotlinx.coroutines.flow.Flow


// Usaremos Flow en lugar de LiveData, ya que estamos usan Kotlin Flow en en el ViewModel

@Dao
interface ResenaDao {
    @Insert
    suspend fun insertarResena(resena: Resena) // Corregido el nombre a 'insertarResena'
    @Query("SELECT * FROM resenas WHERE productoId = :productId ORDER BY fecha DESC")
    fun obtenerResenasPorProducto(productId: String): Flow<List<Resena>> // Usamos Flow<List<Resena>>
    @Query("SELECT AVG(calificacion) FROM resenas WHERE productoId = :productId")
    fun obtenerPromedioCalificacion(productId: String): Flow<Double?>
}