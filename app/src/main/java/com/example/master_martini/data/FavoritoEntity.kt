package com.example.master_martini.data

import androidx.room.Entity

@Entity(
    tableName = "favoritos_table",
    primaryKeys = ["titulo", "usuarioId"]
)
data class FavoritoEntity(
    val titulo: String,
    val descripcion: String,
    val duracion: String,
    val nivel: String,
    val url: String,
    val categoria: String,
    val usuarioId: Int
)
