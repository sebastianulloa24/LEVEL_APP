package com.grupo_7_kotlin.level_app.data.model

// La clase reperesenta un post obtenido desde la API

data class Post(
    val userId: Int,
    val id:Int,
    val title:String,
    val body:String

)
