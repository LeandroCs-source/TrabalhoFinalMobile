package com.example.trabalhofinalmobile.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "usuarios.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE,
                senha TEXT
            );
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    fun inserirUsuario(email: String, senha: String): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("email", email)
            put("senha", senha)
        }

        return try {
            db.insertOrThrow("usuarios", null, valores)
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun verificarUsuarioExistente(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE email = ?", arrayOf(email))
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }
    fun verificarCredenciais(email: String, senha: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE email = ? AND senha = ?",
            arrayOf(email, senha)
        )
        val valido = cursor.count > 0
        cursor.close()
        db.close()
        return valido
    }
}