package com.example.trabalhofinalmobile.bancodedados

import android.content.Context
import android.util.Log
import com.example.trabalhofinalmobile.classes.Livro
import java.sql.SQLException

class LivroDAO (context: Context) {

    //variaveis de escrita e leitura do banco
    val escrita = BancoDadosHelper(context).writableDatabase
    val leitura = BancoDadosHelper(context).readableDatabase

    //função para salvar os dados do livro no banco de dados
    fun salvar(livro: Livro):Boolean{

        var retorno = false
        //sentença sql para inserir os dados
        val sql = "insert into livro values (null, '${livro.titulo}', '${livro.autor}', ${livro.avaliacao}, ${livro.idGenero});"

        try {
            escrita.execSQL(sql) // os dados são salvos no banco
            retorno = true
        }catch (e: SQLException){
            Log.i("info_bd", "Erro ao salvar!")
        }
        return retorno
    }

    //função para editar os dados no banco
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

    //função para excluir dados do banco
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

    //função para retornar todos os livros cadastrados no banco
    fun listar():List<Livro>{

        //lista para armazenar os dados
        val listaLivro = mutableListOf<Livro>()

        val sql = " select livro.idLivro, livro.titulo, livro.autor, livro.avaliacao, livro.idGenero, genero.nomeGenero " +
                "from livro inner join genero on livro.idGenero = genero.idGenero;"


        try{
            val cursor = leitura.rawQuery(sql, null)//leitura do banco de dados

            while(cursor.moveToNext()){
                val idLivro = cursor.getInt(0)
                val titulo = cursor.getString(1)
                val autor = cursor.getString(2)
                val avaliacao = cursor.getInt(3)
                val genero = cursor.getInt(4)
                val nomeGenero = cursor.getString(5)

                //adiciona os dados a lista criada
                listaLivro.add(Livro(idLivro, titulo, autor, avaliacao, genero, nomeGenero))
            }
        }catch (e:SQLException){
            Log.i("info_bd", "Erro ao listar livros!")
        }
        //retorno da lista criada
        return listaLivro
    }
}