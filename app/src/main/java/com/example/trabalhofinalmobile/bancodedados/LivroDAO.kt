package com.example.trabalhofinalmobile.bancodedados

import android.content.Context
import android.util.Log
import com.example.trabalhofinalmobile.classes.Livro
import java.sql.SQLException

class LivroDAO (context: Context) {

    val escrita = BancoDadosHelper(context).writableDatabase
    val leitura = BancoDadosHelper(context).readableDatabase

    fun salvar(livro: Livro):Boolean{

        var retorno = false
        val sql = "insert into livro values (null, '${livro.titulo}', '${livro.autor}', ${livro.avaliacao}, ${livro.idGenero});"

        try {
            escrita.execSQL(sql)
            retorno = true
        }catch (e: SQLException){
            Log.i("info_bd", "Erro ao salvar!")
        }
        return retorno
    }

    fun editar(livro: Livro):Boolean{

        var retorno = false
        val sql = "update livro set titulo = '${livro.titulo}', autor = '${livro.autor}', avaliacao = ${livro.avaliacao}, " +
                "idGenero = ${livro.idGenero} where idLivro = ${livro.idLivro};"

        try {
            escrita.execSQL(sql)
            retorno = true
        }catch (e: SQLException){
            Log.i("info_bd", "Erro ao editar!")
        }
        return retorno
    }

    fun excluir(livro: Livro): Boolean{

        val sql = "delete from livro where idLivro = ${livro.idLivro};"

        try {
            escrita.execSQL(sql)
        }catch (e:Exception){
            Log.i("info_bd", "Erro ao excluir!")
            return false
        }
        return true
    }

    fun listar():List<Livro>{

        val listaLivro = mutableListOf<Livro>()
        val sql = "select * from livro;"

        try{
            val cursor = leitura.rawQuery(sql, null)

            while(cursor.moveToNext()){
                val idLivro = cursor.getInt(0)
                val titulo = cursor.getString(1)
                val autor = cursor.getString(2)
                val avaliacao = cursor.getInt(3)
                val genero = cursor.getInt(4)

                listaLivro.add(Livro(idLivro, titulo, autor, avaliacao, genero))
            }
        }catch (e:SQLException){
            Log.i("info_bd", "Erro ao listar livros")
        }
        return listaLivro
    }
}