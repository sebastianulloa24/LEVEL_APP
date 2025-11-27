package com.grupo_7_kotlin.level_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo_7_kotlin.level_app.data.model.Post
import com.grupo_7_kotlin.level_app.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


// ViewModel va a mantener el estado de los datos obtenidos
class PostsViewModel : ViewModel() {
    private val repository = PostRepository()

// Flujo mutable que contiene la lista

    private val _postList = MutableStateFlow<List<Post>>(emptyList())


// flujo de lectura de la lista con los posts (Publica)

    val postList: StateFlow<List<Post>> = _postList

// Al inicio debo llamar  la lista

    init {
        fetchPosts()
    }

// Funcion que obtiene los datos en segundo plano

    private fun fetchPosts() {
        viewModelScope.launch {
            try {
                _postList.value = repository.getPosts()
            } catch (e: Exception) {
                println("Error al obtener datos: ${e.localizedMessage}")
            }

            // fin try
        } // fin launch
    }// fin private

}// Fin clase







