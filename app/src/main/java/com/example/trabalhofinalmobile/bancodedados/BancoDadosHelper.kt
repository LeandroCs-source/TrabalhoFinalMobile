package com.example.trabalhofinalmobile.bancodedados

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.SQLException

class BancoDadosHelper (context:Context):SQLiteOpenHelper (context, "EstanteInteligente", null, 1) {

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val sqlUsuario = "create table usuario (idUsuario integer not null primary key autoincrement, usuario varchar(50) not null, " +
                "senha varchar(50) not null);"

        val sqlGenero = "create table genero (idGenero integer not null primary key autoincrement, nomeGenero varchar(50) not null); "

        val sqlLivro = "create table livro (idLivro integer not null primary key autoincrement, titulo varchar(50), autor varchar(50)," +
                "avaliacao interg not null, generoId integer not null, foreign key (generoId) references genero(idGenero));"

        try {
            db?.execSQL(sqlUsuario)
            db?.execSQL(sqlGenero)
            db?.execSQL(sqlLivro)
        }catch (e:SQLException){
            e.printStackTrace()
            Log.i("Info_bd", "Erro ao criar tabelas")
        }

        // Inserir gêneros iniciais
        val generosIniciais = listOf("Romance", "Fantasia", "Ficção Científica", "Suspense", "Aventura")
        generosIniciais.forEach { genero ->
            db?.execSQL("insert into genero (nome) values ('$genero')")
        }

    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}