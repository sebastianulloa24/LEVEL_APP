package com.grupo_7_kotlin.level_app.data.remote

import com.grupo_7_kotlin.level_app.data.model.Post
import retrofit2.http.GET

interface ApiService {
   @GET(value = "/post")
   suspend fun getPosts(): List<Post>
}