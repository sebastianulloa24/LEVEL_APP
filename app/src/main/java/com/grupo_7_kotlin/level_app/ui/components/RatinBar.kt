package com.grupo_7_kotlin.level_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float, // La calificación actual (ej: 3.5)
    stars: Int = 5, // Total de estrellas
    starColor: Color = Color(0xFFFFC107),
    onRatingChange: (Float) -> Unit = {} // Función de callback para cambiar la calificación
) {
    // Fila que contiene las estrellas
    Row(modifier = modifier) {
        (1..stars).forEach { index ->
            // Determina si la estrella debe estar completamente llena o vacía
            val isFilled = index <= rating

            Icon(
                // Usa Star (llena) si está calificada, StarBorder (vacía) si no
                imageVector = if (isFilled) Icons.Filled.Star else Icons.Default.StarBorder,
                contentDescription = "$index estrellas", // Texto para accesibilidad
                tint = starColor,
                // Permite hacer clic para cambiar la calificación (si la función fue proporcionada)
                modifier = Modifier
                    .clickable { onRatingChange(index.toFloat()) }
            )
        }
    }
}