package com.example.trabalhofinalmobile.adapter

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalhofinalmobile.R
import com.example.trabalhofinalmobile.classes.Livro

class LivroAdapter (val lista:MutableList<Livro>, private val listener: OnItemClickListener): RecyclerView.Adapter<LivroAdapter.LivroViewHolder>() {

    //interface para ser possível o clique nos botões de editar e excluir
    interface OnItemClickListener {
        fun onEditClick(livro: Livro)
        fun onDeleteClick(livro: Livro)
    }

    //declaração das váriaveis
    inner class LivroViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titulo: TextView = itemView.findViewById(R.id.listaTitulo)
        val autor: TextView = itemView.findViewById(R.id.listaAutor)
        val genero: TextView = itemView.findViewById(R.id.listaGenero)
        val avaliacao: TextView = itemView.findViewById(R.id.listaAvaliacao)
        val containerEditar: LinearLayout = itemView.findViewById(R.id.containerEditar)
        val containerExcluir: LinearLayout = itemView.findViewById(R.id.containerExcluir)

    }

    //conexão com as activitys
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val layouInflater = LayoutInflater.from(parent.context)
        val itemView = layouInflater.inflate(R.layout.lista_de_livros, parent, false)
        return LivroViewHolder(itemView)
    }

    //retorna o tamanho da lista
    override fun getItemCount(): Int {
        return lista.size
    }

    //função para preencher o RecyclerView com os dados que serão apresentados
    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = lista[position]
        holder.titulo.text = livro.titulo
        holder.autor.text = "Autor: ${livro.autor}"
        holder.genero.text = "Gênero: ${livro.nomeGenero}"
        holder.avaliacao.text = "Avaliação: ${livro.avaliacao}"

        holder.containerEditar.setOnClickListener {
            listener.onEditClick(livro)
        }
        holder.containerExcluir.setOnClickListener {
            listener.onDeleteClick(livro)
        }

    }

    //função para atualizar a lista
    fun atualizarLista(novaLista: List<Livro>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }
}