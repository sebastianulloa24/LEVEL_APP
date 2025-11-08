package com.grupo_7_kotlin.level_app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/// Clave foránea al Producto y al Usuario
@Entity(tableName = "resenas",
    foreignKeys = [
        ForeignKey(
            entity = Producto::class,
            parentColumns = ["id"],
            childColumns = ["productoId"],
            onDelete = ForeignKey.CASCADE // Si el producto se borra, se borran las reseñas
        ),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE // Si el usuario se borra, se borran sus reseñas
        )
    ],
    // --- ESTA ES LA CORRECCIÓN ---
    indices = [
        Index(value = ["productoId"]),
        Index(value = ["usuarioId"])
    ]
    // --- FIN DE LA CORRECIÓN ---
)
data class Resena(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: String, // ID del producto reseñado
    val usuarioId: Int,     // ID del usuario que reseñó
    val calificacion: Int,  // Puntuación de 1 a 5
    val comentario: String, // Texto de la reseña
    val fecha: Long = System.currentTimeMillis() // Fecha de la reseña
)