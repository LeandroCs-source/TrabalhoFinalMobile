package com.example.trabalhofinalmobile.adapter

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalhofinalmobile.R
import com.example.trabalhofinalmobile.classes.Livro

class LivroAdapter (val lista:MutableList<Livro>): RecyclerView.Adapter<LivroAdapter.LivroViewHolder>() {

    inner class LivroViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titulo: TextView = itemView.findViewById(R.id.listaTitulo)
        val autor: TextView = itemView.findViewById(R.id.listaAutor)
        val genero: TextView = itemView.findViewById(R.id.listaGenero)
        val avaliacao: TextView = itemView.findViewById(R.id.listaAvaliacao)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val layouInflater = LayoutInflater.from(parent.context)
        val itemView = layouInflater.inflate(R.layout.lista_de_livros, parent, false)
        return LivroViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = lista[position]
        holder.titulo.text = livro.titulo
        holder.autor.text = "Autor: ${livro.autor}"
        holder.genero.text = "Gênero: ${livro.nomeGenero}"
        holder.avaliacao.text = "Avaliação: ${livro.avaliacao}"

    }

    fun atualizarLista(novaLista: List<Livro>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }
}