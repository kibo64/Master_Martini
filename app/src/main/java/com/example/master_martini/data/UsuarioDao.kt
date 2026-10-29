package com.example.master_martini.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuario_table ORDER BY id ASC LIMIT 1")
    fun obtenerUsuario(): Flow<UsuarioEntity?>

    @Query("SELECT * FROM usuario_table WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): UsuarioEntity?

    @Query("SELECT * FROM usuario_table ORDER BY id ASC")
    fun obtenerUsuarios(): Flow<List<UsuarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarUsuario(usuario: UsuarioEntity): Long

    @Query("DELETE FROM usuario_table WHERE id = :id")
    suspend fun eliminarUsuarioPorId(id: Int)

    @Query("DELETE FROM usuario_table")
    suspend fun eliminarUsuario()
}
