package com.grupo_7_kotlin.level_app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grupo_7_kotlin.level_app.data.database.AppDatabase
import java.lang.IllegalArgumentException

// Esta clase le dice a Compose cómo crear el UserViewModel con el contexto de Android
class UsuarioViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {

            // Obtener la instancia de la base de datos (Singleton)
            val database = AppDatabase.getDatabase(context)

            // Pasar el DAO al ViewModel
            @Suppress("UNCHECKED_CAST")
            return UsuarioViewModel(database.usuarioDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}