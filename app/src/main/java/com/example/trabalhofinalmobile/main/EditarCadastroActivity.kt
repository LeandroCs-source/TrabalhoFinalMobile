package com.example.trabalhofinalmobile.main

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
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
import com.example.trabalhofinalmobile.databinding.ActivityEditarCadastroBinding

class EditarCadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEditarCadastroBinding.inflate(layoutInflater)
    }

    //cria uma lista para guardar os gêneros
    private var listaGeneros: List<Genero> = listOf()

    //cria uma variável para guardar o livro original
    private var livroParaEditar: Livro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //configurações iniciais para popular os spinners
        carregarGeneros()
        configurarSpinnerAvaliacao()

        //recupera os dados enviados da tela principal
        recuperarePreencherDados()

        //botão para salvar a edição dos dados
        binding.btnEditarLivro.setOnClickListener{
            salvarAlteracoes()
        }

        //botão de voltar
        binding.btnEditarVoltar.setOnClickListener{
            finish()
        }

    }

    //função para realizar as alterações no cadastro do livro
    private fun salvarAlteracoes() {

        //faz uma cópia do livro original
        val livroOriginal = livroParaEditar

        if(livroOriginal == null){
            Toast.makeText(this, "Erro: Não foi possível carregar os dados do livro.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        //captura os dados que foram editados
        val novoTitulo = binding.inputEditarTitulo.text.toString().trim()
        val novoAutor = binding.inputEditarAutor.text.toString().trim()

        if(novoTitulo.isEmpty() || novoAutor.isEmpty()){
            Toast.makeText(this, "Título e Autor não podem estar vazios.", Toast.LENGTH_SHORT).show()
            return
        }

        val posicaoGenero = binding.spinnerEditarGenero.selectedItemPosition
        val generoSelecionado = listaGeneros[posicaoGenero]
        val novaAvaliacao = binding.spinnerEditarAvalicacao.selectedItem.toString().toInt()

        // Criação de um novo objeto Livro com os dados atualizados
        val livroAtualizado = Livro(
            idLivro = livroOriginal.idLivro, //mantém o mesmo ID do livro original
            titulo = novoTitulo,
            autor = novoAutor,
            avaliacao = novaAvaliacao,
            idGenero = generoSelecionado.idGenero,
            nomeGenero = generoSelecionado.nomeGenero
        )

        //Usa o DAO para atualizar no banco de dados
        val livroDAO = LivroDAO(this)

        if (livroDAO.editar(livroAtualizado)) {
            Toast.makeText(this, "Livro atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            // Retorna um resultado OK para a tela anterior saber que a atualização ocorreu
            setResult(Activity.RESULT_OK)
        } else {
            Toast.makeText(this, "Erro ao atualizar o livro.", Toast.LENGTH_SHORT).show()
        }
    }

    //função para recuperar os dados enviados da tela principal
    private fun recuperarePreencherDados() {
        val bundle = intent.extras

        if (bundle != null) {
            val livro = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("livro", Livro::class.java)
            } else {

                bundle.getParcelable("livro")
            }

            //guarda os dados enviados
            livroParaEditar = livro

            //preenchimentos dos dados enviados na tela de edição
            livro?.let {livro->
                binding.inputEditarTitulo.setText(livro.titulo)
                binding.inputEditarAutor.setText(livro.autor)

                val posicaoAvaliacao = livro.avaliacao - 1

                binding.spinnerEditarAvalicacao.setSelection(posicaoAvaliacao)

                val posicaoGenero = listaGeneros.indexOfFirst { it.idGenero == livro.idGenero }

                if (posicaoGenero != -1) { // Garante que o gênero foi encontrado
                    binding.spinnerEditarGenero.setSelection(posicaoGenero)
                }
            }
        }
    }

    //função para preencher os valores do spinner de avaliação
    private fun configurarSpinnerAvaliacao() {
        val avaliacoes = listOf("1", "2", "3", "4", "5")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, avaliacoes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEditarAvalicacao.adapter = adapter
    }

    //função para preencher os dados dos generos a partir do DAO
    private fun carregarGeneros() {
        val generoDAO = GeneroDAO(this)
        listaGeneros = generoDAO.listar()

        val nomesGeneros = listaGeneros.map { it.nomeGenero }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nomesGeneros)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerEditarGenero.adapter = adapter
    }
}
