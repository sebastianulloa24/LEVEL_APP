package com.grupo_7_kotlin.level_app.data.repository

import android.content.Context
import com.grupo_7_kotlin.level_app.data.model.Usuario

class UsuarioRepository(private val context: Context) {

    private val listaUsuarios = mutableListOf<Usuario>()

    fun guardarUsuario(usuario: Usuario) {
        // Aquí podrías guardar en Room o SharedPreferences
        listaUsuarios.add(usuario)
        // Si quieres persistir en base de datos, aquí iría la lógica
    }

    fun obtenerUsuarios(): List<Usuario> {
        return listaUsuarios
    }
}
