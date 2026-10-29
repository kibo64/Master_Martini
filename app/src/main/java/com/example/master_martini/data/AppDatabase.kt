package com.example.master_martini.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        UsuarioEntity::class,
        FavoritoEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun favoritoDao(): FavoritoDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE usuario_table ADD COLUMN passwordHash TEXT NOT NULL DEFAULT ''"
                )
                db.execSQL(
                    "ALTER TABLE usuario_table ADD COLUMN passwordSalt TEXT NOT NULL DEFAULT ''"
                )
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE usuario_table_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nombre TEXT NOT NULL,
                        correo TEXT NOT NULL,
                        fotoUri TEXT NOT NULL,
                        passwordHash TEXT NOT NULL DEFAULT '',
                        passwordSalt TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO usuario_table_new
                    (id, nombre, correo, fotoUri, passwordHash, passwordSalt)
                    SELECT id, nombre, correo, fotoUri, passwordHash, passwordSalt
                    FROM usuario_table
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE usuario_table")
                db.execSQL(
                    "ALTER TABLE usuario_table_new RENAME TO usuario_table"
                )
            }
        }
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE favoritos_table_new (
                        titulo TEXT NOT NULL,
                        descripcion TEXT NOT NULL,
                        duracion TEXT NOT NULL,
                        nivel TEXT NOT NULL,
                        url TEXT NOT NULL,
                        categoria TEXT NOT NULL,
                        usuarioId INTEGER NOT NULL,
                        PRIMARY KEY(titulo, usuarioId)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO favoritos_table_new
                    (titulo, descripcion, duracion, nivel, url, categoria, usuarioId)
                    SELECT titulo, descripcion, duracion, nivel, url, categoria, 1
                    FROM favoritos_table
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE favoritos_table")
                db.execSQL(
                    "ALTER TABLE favoritos_table_new RENAME TO favoritos_table"
                )
            }
        }
    fun getDatabase(context: Context): AppDatabase =
        INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "master_martini_db"
            )
                .addMigrations(
                    MIGRATION_2_3,
                    MIGRATION_3_4,
                    MIGRATION_4_5
                )
                .build()
            INSTANCE = instance
            instance
        }
    }
}
