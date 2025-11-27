package com.grupo_7_kotlin.level_app.data.repository

import com.grupo_7_kotlin.level_app.data.model.Post
import com.grupo_7_kotlin.level_app.data.remote.RetrofitInstance


// Este repositorio se encarga de accedder a los datos usando Retrofit


class  PostRepository {
    //Funcion que obtenga los post desde la API

    suspend fun getPosts():List<Post>{
        return RetrofitInstance.api.getPosts()

    }

}