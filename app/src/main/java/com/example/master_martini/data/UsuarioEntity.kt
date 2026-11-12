package com.example.master_martini.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario_table")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val fotoUri: String,
    val passwordHash: String = "",
    val passwordSalt: String = ""
)
