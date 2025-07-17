package com.example.trabalhofinalmobile.bancodedados

import android.content.Context
import android.util.Log
import com.example.trabalhofinalmobile.classes.Genero
import java.sql.SQLException

class GeneroDAO(context: Context) {

    //variaveis de escrita e leitura do banco
    val escrita = BancoDadosHelper(context).writableDatabase
    val leitura = BancoDadosHelper(context).readableDatabase

    //função para salvar  os dados dos gêneros
    fun salvar(genero: Genero): Boolean{

        var retorno = false

        val sql = "insert into genero values (null, '${genero.nomeGenero}');"

        try {
            escrita.execSQL(sql)
            retorno = true
        }catch (e: SQLException){
            Log.i("info_bd", "Erro ao salvar!")
        }
        return retorno
    }

    //função para fazer a listagem dos gêneros cadastrados
    fun listar():List<Genero>{

        //variável para guardar a lista
        val listaGenero = mutableListOf<Genero>()
        //consulta a tabela gênero no banco
        val sql = "select * from genero;"

        try {
            //variável para guardar os dados lidos do banco
            val cursor = leitura.rawQuery(sql, null)

            //laço para percorrer a lista retornada
            while (cursor.moveToNext()){
                val idGenero = cursor.getInt(0)
                val genero = cursor.getString(1)

                //adiciona os dados lidos na lista criada
                listaGenero.add(Genero(idGenero, genero))
            }
        }catch (e:SQLException){
            Log.i("info_bd", "Erro ao listar gêneros!")
        }
        //a lista de gêneros consultada é retornada pela função
        return listaGenero
    }
}