package com.example.trabalhofinalmobile.bancodedados

import android.content.Context
import android.util.Log
import com.example.trabalhofinalmobile.classes.Genero
import java.sql.SQLException

class GeneroDAO(context: Context) {

    val escrita = BancoDadosHelper(context).writableDatabase
    val leitura = BancoDadosHelper(context).readableDatabase

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

    fun listar():List<Genero>{

        val listaGenero = mutableListOf<Genero>()
        val sql = "select * from genero;"

        try {
            val cursor = leitura.rawQuery(sql, null)

            while (cursor.moveToNext()){
                val idGenero = cursor.getInt(0)
                val genero = cursor.getString(1)

                listaGenero.add(Genero(idGenero, genero))
            }
        }catch (e:SQLException){
            Log.i("info_bd", "Erro ao listar gêneros")
        }
        return listaGenero
    }
}