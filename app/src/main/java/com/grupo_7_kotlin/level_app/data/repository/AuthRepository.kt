package com.grupo_7_kotlin.level_app.data.repository

import com.grupo_7_kotlin.level_app.data.model.Credential

class AuthRepository(
    private val validCredential: Credential = Credential.Admin
) {
    fun login(username: String, password: String): Boolean {
        return username == validCredential.username && password == validCredential.password
    }

}