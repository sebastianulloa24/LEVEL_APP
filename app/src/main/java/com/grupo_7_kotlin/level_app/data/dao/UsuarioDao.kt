package com.grupo_7_kotlin.level_app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.grupo_7_kotlin.level_app.data.model.Usuario

@Dao // 🔹 OBLIGATORIO: le dice a Room que esta interfaz es un DAO
interface UsuarioDao {

    @Insert
    suspend fun insertarUsuario(usuario: Usuario)
    @Query("SELECT * FROM usuario WHERE email = :email LIMIT 1")
    suspend fun obtenerUsuarioPorEmail(email: String): Usuario?
    @Query("SELECT * FROM usuario WHERE myReferralCode = :code LIMIT 1")
    suspend fun getUserByReferralCode(code: String): Usuario?
    @Update
    suspend fun updateUser(usuario: Usuario)
    @Query("DELETE FROM usuario")
    suspend fun deleteAllUsers()



}
