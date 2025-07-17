package com.example.trabalhofinalmobile.main

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalhofinalmobile.bancodedados.UsuarioDAO
import com.example.trabalhofinalmobile.classes.Usuario
import com.example.trabalhofinalmobile.databinding.ActivityCadastrarUsuarioBinding

class CadastrarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarUsuarioBinding
    private lateinit var usuarioDAO: UsuarioDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioDAO = UsuarioDAO(this)

        binding.btnCadastrarUsuario.setOnClickListener {
            val email = binding.inputCadastroEmail.text.toString().trim()
            val senha = binding.inputCadastroSenha.text.toString().trim()

            // Verifica se os campos estão vazios
            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
            // Verifica se o e-mail tem formato válido
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show()
            }
            // Verifica se o e-mail já está cadastrado no banco
            else if (usuarioDAO.verificarUsuarioExistente(email)) {
                Toast.makeText(this, "E-mail já cadastrado!", Toast.LENGTH_SHORT).show()
            }
            else {
                // Cria um novo usuário
                val novoUsuario = Usuario(usuario = email, senha = senha)

                val sucesso = usuarioDAO.salvar(novoUsuario)

                if (sucesso) {
                    Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    binding.inputCadastroEmail.text?.clear()
                    binding.inputCadastroSenha.text?.clear()

                    // Volta para tela de login
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao cadastrar usuário!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
