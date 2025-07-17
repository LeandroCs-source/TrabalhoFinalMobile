package com.example.trabalhofinalmobile.bancodedados

import android.content.Context
import android.util.Log
import com.example.trabalhofinalmobile.classes.Usuario
import java.sql.SQLException

class UsuarioDAO(context: Context) {

    //variaveis de escrita e leitura do banco
    val escrita = BancoDadosHelper(context).writableDatabase
    val leitura = BancoDadosHelper(context).readableDatabase

    //função para salvar os dados do usuário na tabela usuário
    fun salvar(usuario: Usuario): Boolean{

        var retorno = false
        val sql = "insert into usuario values(null, '${usuario.usuario}', '${usuario.senha}');"

        try {
            escrita.execSQL(sql)
            retorno = true
        }catch (e: SQLException){
            Log.i("info_bd", "Erro ao salvar!")
        }
        return retorno
    }

    //função para autenticar que e-mail e usuário
    fun autenticar( usuario:String, senha:String): Boolean{

        val sql = "select * from usuario where usuario = '${usuario}' and senha ='${senha}';"

        try {
            val cursor = leitura.rawQuery(sql, null)
            val autenticado = cursor.count > 0
            cursor.close()
            return autenticado
        }catch (e:SQLException){
            Log.i("info_bd", "Erro ao autenticar!")
            return false
        }
    }

}