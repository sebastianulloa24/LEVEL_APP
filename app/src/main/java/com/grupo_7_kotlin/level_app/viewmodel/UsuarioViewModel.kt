package com.grupo_7_kotlin.level_app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.grupo_7_kotlin.level_app.data.model.Usuario
import com.grupo_7_kotlin.level_app.data.repository.UsuarioRepository

class UsuarioViewModel(context: Context) : ViewModel() {

    private val repository = UsuarioRepository(context)

    fun registrarUsuario(usuario: Usuario): String {
        // Validación de edad
        if (usuario.edad < 18) {
            return "Debes tener al menos 18 años para registrarte."
        }

        // Validación de descuento por correo Duoc
        val descuento = if (usuario.correo.contains("@duoc.cl")) 20 else 0
        val usuarioConDescuento = usuario.copy(descuento = descuento)

        repository.guardarUsuario(usuarioConDescuento)

        return if (descuento == 20)
            "Registro exitoso. Se aplicó un 20% de descuento permanente."
        else
            "Registro exitoso. Ahora puedes iniciar sesión."
    }
}
