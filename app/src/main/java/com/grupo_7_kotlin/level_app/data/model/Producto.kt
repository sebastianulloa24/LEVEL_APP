package com.grupo_7_kotlin.level_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey val id: String, // Ej: JM001
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val descripcion: String,
    val urlImagen: String = ""
)