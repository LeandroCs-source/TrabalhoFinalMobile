package com.example.trabalhofinalmobile.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.trabalhofinalmobile.R
import com.example.trabalhofinalmobile.adapter.LivroAdapter
import com.example.trabalhofinalmobile.bancodedados.GeneroDAO
import com.example.trabalhofinalmobile.bancodedados.LivroDAO
import com.example.trabalhofinalmobile.classes.Genero
import com.example.trabalhofinalmobile.classes.Livro


class AcervoActivity : AppCompatActivity() {

    private lateinit var spinnerGenero: Spinner
    private lateinit var editBuscarAutor: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var livroAdapter: LivroAdapter

    private var listaTodosLivros: List<Livro> = listOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acervo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinnerGenero = findViewById(R.id.spinnerGenero)
        editBuscarAutor = findViewById(R.id.editBuscarAutor)
        recyclerView = findViewById(R.id.recyclerLivros)

        // Inicializar adapter
        livroAdapter = LivroAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = livroAdapter

        carregarGeneros()
        carregarLivros()

        // Filtro por autor conforme digita
        editBuscarAutor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = aplicarFiltros()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Listener para Spinner (filtro por gênero)
        spinnerGenero.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                aplicarFiltros()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}//caso nada seja selecionado
        }

        startActivity(Intent(this, CadastrarLivroActivity::class.java))
    }

    private fun carregarGeneros() {
        val generoDAO = GeneroDAO(this)
        val listaGeneros = generoDAO.listar()
        val nomesGeneros = mutableListOf("Todos os Gêneros")
        nomesGeneros.addAll(listaGeneros.map { it.nomeGenero })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nomesGeneros)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = adapter
    }

    private fun carregarLivros() {
        val livroDAO = LivroDAO(this)
        listaTodosLivros = livroDAO.listar() // ou seu método para buscar todos
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        val generoSelecionado = spinnerGenero.selectedItem.toString()
        val autorBuscado = editBuscarAutor.text.toString().trim()

        val listaFiltrada = listaTodosLivros.filter { livro ->
            val combinaGenero = (generoSelecionado == "Todos os Gêneros" || livro.nomeGenero == generoSelecionado)
            val combinaAutor = autorBuscado.isBlank() || livro.autor.contains(autorBuscado, ignoreCase = true)
            combinaGenero && combinaAutor
        }

        livroAdapter.atualizarLista(listaFiltrada)
    }
}