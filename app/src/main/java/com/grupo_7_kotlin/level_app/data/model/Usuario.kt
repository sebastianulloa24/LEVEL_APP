package com.grupo_7_kotlin.level_app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(index = true) val email: String,
    val passwordHash: String, // ¡Nunca guardes passwordHashs en texto plano!
    val birthDate: Long, // Guardamos la fecha (Long) para facilitar el cálculo de edad
    val hasDuocDiscount: Boolean = false, // Para el descuento del 20%
    val levelUpPoints: Int = 0, // Para gamificación
    val myReferralCode: String // Un código único que este usuario puede compartir

)