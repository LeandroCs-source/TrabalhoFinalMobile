package com.example.trabalhofinalmobile.classes

data class Livro(
    val idLivro : Int,
    val titulo : String,
    val autor : String,
    val idGenero : Int,
    val avaliacao : Double,
    val nomeGenero : String? = null
)
