package com.example.master_martini.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario_table")
data class UsuarioEntity(
    @PrimaryKey val id: Int = 1,
    val nombre: String,
    val correo: String,
    val fotoUri: String
)