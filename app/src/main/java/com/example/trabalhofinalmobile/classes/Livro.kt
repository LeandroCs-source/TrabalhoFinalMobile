package com.example.trabalhofinalmobile.classes

import java.io.Serializable

data class Livro(
    val idLivro : Int,
    val titulo : String,
    val autor : String,
    val avaliacao : Int,
    val idGenero : Int,
    val nomeGenero: String
):Serializable // usando para transmitir informações entre as activitys para fazer as edições
