package com.example.master_martini.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {

    @Query("SELECT * FROM favoritos_table WHERE usuarioId = :usuarioId ORDER BY titulo ASC")
    fun obtenerFavoritos(usuarioId: Int): Flow<List<FavoritoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarFavorito(favorito: FavoritoEntity)

    @Query("DELETE FROM favoritos_table WHERE titulo = :titulo AND usuarioId = :usuarioId")
    suspend fun eliminarFavorito(titulo: String, usuarioId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favoritos_table WHERE titulo = :titulo AND usuarioId = :usuarioId)")
    suspend fun esFavorito(titulo: String, usuarioId: Int): Boolean
}
