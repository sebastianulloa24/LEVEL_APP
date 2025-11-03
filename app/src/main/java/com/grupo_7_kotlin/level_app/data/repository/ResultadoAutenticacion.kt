package com.grupo_7_kotlin.level_app.data.repository

/**
 * Clase Sellada (Sealed Class) para representar el resultado de las
 * operaciones de autenticación (Login y Registro).
 * * Permite a la UI manejar todos los posibles estados de forma exhaustiva
 * usando una sentencia 'when'.
 */
sealed class ResultadoAutenticacion {

    /**
     * Estado de éxito: La operación se completó correctamente.
     */
    object Exito : ResultadoAutenticacion()

    /**
     * Estado de error: La operación falló y contiene un mensaje descriptivo.
     * @param mensaje El mensaje de error a mostrar al usuario (ej: "Credenciales incorrectas").
     */
    data class Error(val mensaje: String) : ResultadoAutenticacion()

    // Podrías añadir un 'object Cargando : AuthRepository()' si quisieras
    // mostrar un spinner mientras se espera la respuesta de Room.
}