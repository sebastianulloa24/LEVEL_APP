package com.grupo_7_kotlin.level_app.ui.login

// Sirve para manejar los datos de la UI que
// necesita mostrar o controlar

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
