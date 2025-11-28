package com.grupo_7_kotlin.level_app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.grupo_7_kotlin.level_app.data.model.Usuario

@Dao
interface UsuarioDao {

    // INSERT seguro (evita conflictos)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)

    // Obtener usuario por email
    @Query("SELECT * FROM usuario WHERE email = :email LIMIT 1")
    suspend fun obtenerUsuarioPorEmail(email: String): Usuario?

    // Obtener usuario por código de referido
    @Query("SELECT * FROM usuario WHERE myReferralCode = :code LIMIT 1")
    suspend fun getUserByReferralCode(code: String): Usuario?

    // Actualizar usuario
    @Update
    suspend fun updateUser(usuario: Usuario)

    // Borrar todos los usuarios (para tests)
    @Query("DELETE FROM usuario")
    suspend fun deleteAllUsers()
}
