package com.example.trabalhofinalmobile.main

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trabalhofinalmobile.R
import com.example.trabalhofinalmobile.bancodedados.GeneroDAO
import com.example.trabalhofinalmobile.bancodedados.LivroDAO
import com.example.trabalhofinalmobile.classes.Genero
import com.example.trabalhofinalmobile.classes.Livro
import com.example.trabalhofinalmobile.databinding.ActivityCadastrarLivroBinding

class CadastrarLivroActivity : AppCompatActivity() {

    val binding by lazy{
        ActivityCadastrarLivroBinding.inflate(layoutInflater)
    }

    //criação da lista para guardar os gêneros
    private var listaGeneros: List<Genero> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //carregamentos dos dados a partir do banco de dados
        carregarGeneros()
        //preenchimento do spinner de avaliação
        configurarSpinnerAvaliacao()

        //botão para cadastrar um novo livro
        binding.btnCadastrarLivro.setOnClickListener{
            cadastrarLivro()
        }

        //botão para retornar a tela principal
        binding.btnVoltarLivro.setOnClickListener{
            finish()
        }
    }

    //função para cadastrar um novo livro
    private fun cadastrarLivro() {
        //leitura dos dados da activity
        val idLivro = -1
        val titulo = binding.inputCadastroTitulo.text.toString().trim()
        val autor = binding.inputCadastroAutor.text.toString().trim()
        val generoSelecionado = binding.spinnerCadastroGenero.selectedItemPosition
        val avaliacaoSelecionada = binding.spinnerCadastroAvalicacao.selectedItemPosition.toString().toInt()
        val genero = listaGeneros[generoSelecionado]

        //verificação de campos vazios
        if(titulo.isEmpty() || autor.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }
        //criação de um objeto Livro
        val novoLivro = Livro(
            idLivro = 0,
            titulo = titulo,
            autor = autor,
            avaliacao = avaliacaoSelecionada + 1,
            idGenero = genero.idGenero,
            nomeGenero = genero.nomeGenero
        )

        //criação de um objeto DAO para realizar o cadastro no banco
        val livroDAO = LivroDAO(this)

        val salvar = livroDAO.salvar(novoLivro)

        if (salvar) {
            Toast.makeText(this, "Livro cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            limparCampos()
        } else {
            Toast.makeText(this, "Erro ao cadastrar o livro", Toast.LENGTH_SHORT).show()
        }

    }

    //função para limpar os campos após o cadastro
    private fun limparCampos() {
        binding.inputCadastroTitulo.setText("")
        binding.inputCadastroAutor.setText("")
        binding.spinnerCadastroGenero.setSelection(0)
        binding.spinnerCadastroAvalicacao.setSelection(0)
    }

    //função para configurar os valores das avaliações no spinner
    private fun configurarSpinnerAvaliacao() {
        val avaliacoes = listOf("1", "2", "3", "4", "5")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, avaliacoes)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerCadastroAvalicacao.adapter = adapter
    }

    //função para carregar os gêneros no spinner Gêneros
    private fun carregarGeneros() {
        val generoDAO = GeneroDAO(this)
        listaGeneros = generoDAO.listar()

        val nomesGeneros = listaGeneros.map {it.nomeGenero}
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nomesGeneros)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerCadastroGenero.adapter = adapter
    }
}