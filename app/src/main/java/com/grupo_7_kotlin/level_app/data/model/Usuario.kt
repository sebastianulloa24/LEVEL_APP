package com.grupo_7_kotlin.level_app.data.model

data class Usuario(
    val nombre: String,
    val correo: String,
    val edad: Int,
    val descuento: Int = 0
)
