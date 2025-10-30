package com.grupo_7_kotlin.level_app.data.model

data class Credential(val username:String, val password:String){
    // objeto que permite accedr a la instancia de la clase
    companion object{
        val Admin =Credential(username="admin", password="123")
    }

}