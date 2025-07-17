package com.example.trabalhofinalmobile.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.trabalhofinalmobile.R
import com.example.trabalhofinalmobile.adapter.LivroAdapter
import com.example.trabalhofinalmobile.bancodedados.GeneroDAO
import com.example.trabalhofinalmobile.bancodedados.LivroDAO
import com.example.trabalhofinalmobile.classes.Livro
import java.io.Serializable


class AcervoActivity : AppCompatActivity(), LivroAdapter.OnItemClickListener {
    //declaração das variáveis
    private lateinit var spinnerGenero: Spinner
    private lateinit var editBuscarAutor: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var livroAdapter: LivroAdapter
    private lateinit var botaoAdicionar: Button
    private lateinit var botaoSair: Button
    private lateinit var livroLauncher: ActivityResultLauncher<Intent>


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

        //inicialização das variáveis a partir da tela
        spinnerGenero = findViewById(R.id.spinnerGenero)
        editBuscarAutor = findViewById(R.id.editBuscarAutor)
        recyclerView = findViewById(R.id.recyclerLivros)
        botaoAdicionar = findViewById(R.id.btnCadastrarAcervo)
        botaoSair = findViewById(R.id.btnCadastrarSair)

        // Inicialização do Adapter
        livroAdapter = LivroAdapter(mutableListOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = livroAdapter //conexão do recyclerview com a classe Adapter

        //carregamentos dos dados a partir do banco de dados
        carregarGeneros()
        carregarLivros()

        //aplicação dos filtros
        aplicarFiltros()


        // Filtro por autor conforme digitado
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


        //retorna da activity de cadastro se o cadastro foi realizado
        livroLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                carregarLivros()// Recarrega a lista após o retorno da tela de cadastro
                aplicarFiltros()
            }
        }

        //ação do botão que direciona para tela de cadastro
        botaoAdicionar.setOnClickListener {
            val intent = Intent(this, CadastrarLivroActivity::class.java)
            livroLauncher.launch(intent)
        }

        botaoSair.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onEditClick(livro: Livro) {
        // Abre a tela para editar cadastro passando os dados do livro para edição
        val intent = Intent(this, EditarCadastroActivity::class.java)
        intent.putExtra("livro", livro) // Usa a chave para recuperar na outra tela
        livroLauncher.launch(intent)
    }

    override fun onDeleteClick(livro: Livro) {

        AlertDialog.Builder(this) // Cria um diálogo de confirmação para evitar exclusões acidentais
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir o livro '${livro.titulo}'?")
            .setPositiveButton("Sim") { dialog, which ->

                val livroDAO = LivroDAO(this)

                if (livroDAO.excluir(livro)) {
                    Toast.makeText(this, "Livro excluído com sucesso!", Toast.LENGTH_SHORT).show()

                    carregarLivros() // Recarrega a lista para refletir a exclusão na tela
                    aplicarFiltros()
                } else {
                    Toast.makeText(this, "Erro ao excluir o livro.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Não", null).setIcon(android.R.drawable.ic_dialog_alert).show()// Não faz nada se clicar em "Não"
    }


    //função para buscar gêneros do banco de dados
    private fun carregarGeneros() {

        val generoDAO = GeneroDAO(this)//criação de objeto Gênero

        val listaGeneros = generoDAO.listar() //lista dos gêneros presente no banco

        val nomesGeneros = mutableListOf("Todos os Gêneros")//cria uma lista para guardas os dados retornados

        nomesGeneros.addAll(listaGeneros.map { it.nomeGenero })

        //utilização do Adapter para preencher o campo do spinner de Gênero
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nomesGeneros)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = adapter

        // Garante que "Todos os Gêneros" esteja selecionado e chama os filtros
        spinnerGenero.setSelection(0)
    }

    //função para buscar os livros presentes no banco
    private fun carregarLivros() {
        val livroDAO = LivroDAO(this)
        listaTodosLivros = livroDAO.listar() //lista dos livros cadastrados

    }

    //função que aplica os filtros de Gênero e Autor
    private fun aplicarFiltros() {

        val generoSelecionado = spinnerGenero.selectedItem?.toString()
        val autorBuscado = editBuscarAutor.text.toString().trim()

        val listaFiltrada = listaTodosLivros.filter { livro ->
            val combinaGenero = (generoSelecionado == "Todos os Gêneros" || livro.nomeGenero == generoSelecionado)
            val combinaAutor = autorBuscado.isBlank() || livro.autor.contains(autorBuscado, ignoreCase = true)
            combinaGenero && combinaAutor
        }
        //atualização da lista na tela
        livroAdapter.atualizarLista(listaFiltrada)
    }

}