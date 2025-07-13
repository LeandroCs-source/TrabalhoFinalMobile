package com.example.trabalhofinalmobile.bancodedados

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.SQLException

class BancoDadosHelper (context:Context):SQLiteOpenHelper (context, "EstanteInteligente", null, 1) {

    //habilitação para o uso de foreign key
    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }

    //criação do banco de dados
    override fun onCreate(db: SQLiteDatabase?) {

        //criação da tabela usuário
        val sqlUsuario = "create table usuario (idUsuario integer not null primary key autoincrement, usuario varchar(50) not null, " +
                "senha varchar(50) not null);"

        //criação da tabela gênero
        val sqlGenero = "create table genero (idGenero integer not null primary key autoincrement, nomeGenero varchar(50) not null); "

        //criação da tabela livro com relacionamento da tabela genero
        val sqlLivro = "create table livro (idLivro integer not null primary key autoincrement, titulo varchar(50), autor varchar(50)," +
                "avaliacao integer not null, idGenero integer not null, foreign key (idGenero) references genero(idGenero));"

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
            db?.execSQL("insert into genero (nomeGenero) values ('$genero')")
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}