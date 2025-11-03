package com.grupo_7_kotlin.level_app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grupo_7_kotlin.level_app.data.database.AppDatabase


// Clase Factory que permite crear instancias de ProductoViewModel
class ProductoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    // Suprime la advertencia de tipo sin verificar
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            // Accede a la instancia singleton de la base de datos
            val database = AppDatabase.getDatabase(context)
            // Obtiene el ProductoDAO (ya lo tenías)
            val productoDao = database.productoDao()
            val resenaDao = database.resenaDao()
            //se agregan ambos daos
            return ProductoViewModel(productoDao, resenaDao) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}