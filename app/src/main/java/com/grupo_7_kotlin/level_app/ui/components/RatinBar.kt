package com.grupo_7_kotlin.level_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float, // La calificación actual (ej: 3.5)
    stars: Int = 5, // Total de estrellas
    starColor: Color = Color(0xFFFFC107),
    onRatingChange: (Float) -> Unit = {} // Callback para cambios
) {
    Row(modifier = modifier) {
        (1..stars).forEach { index ->
            val isFilled = index <= rating
            Icon(
                imageVector = if (isFilled) Icons.Filled.Star else Icons.Default.StarBorder,
                contentDescription = "$index estrellas",
                tint = starColor,
                modifier = Modifier.clickable { onRatingChange(index.toFloat()) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingBarPreview() {
    val (rating, setRating) = remember { mutableStateOf(3f) }

    RatingBar(
        rating = rating,
        onRatingChange = { newRating -> setRating(newRating) }
    )
}
