package com.example.trabalhofinalmobile.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Livro(
    val idLivro : Int,
    val titulo : String,
    val autor : String,
    val avaliacao : Int,
    val idGenero : Int,
    val nomeGenero: String
): Parcelable // usando para transmitir informações entre as activitys para fazer as edições
