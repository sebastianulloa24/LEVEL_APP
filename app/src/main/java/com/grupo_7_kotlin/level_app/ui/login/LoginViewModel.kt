package com.grupo_7_kotlin.level_app.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.grupo_7_kotlin.level_app.data.repository.AuthRepository

class LoginViewModel (
    private val repo: AuthRepository= AuthRepository()
): ViewModel(){

    var uiState by mutableStateOf(LoginUiState())

    fun onUsernameChange(value:String){
        uiState=uiState.copy(username=value, error=null)
    }

    fun onPasswordChange(value:String){
        uiState=uiState.copy(password=value, error=null)
    }
// Funciones de actualizacion que se llaman desde TextFiel de la Ui


    fun submit(onSuccess:(String) -> Unit){
        uiState =uiState.copy(isLoading=true, error=null)

        val oK = repo.login(uiState.username.trim(), uiState.password)

        uiState =uiState.copy(isLoading=false, error=null)

        if(oK) onSuccess(uiState.username.trim())
        else uiState =uiState.copy(error="Credenciales Invalidas")

    }



}// fin viewmodel