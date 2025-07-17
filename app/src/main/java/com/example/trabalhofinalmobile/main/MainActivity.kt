package com.example.trabalhofinalmobile.main

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalhofinalmobile.databinding.ActivityMainBinding
import com.example.trabalhofinalmobile.bancodedados.UsuarioDAO

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usuarioDAO: UsuarioDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o DAO
        usuarioDAO = UsuarioDAO(this)

        // Texto sublinhado "Cadastre aqui"
        val texto = "Primeiro acesso? Cadastre aqui"
        val spannable = SpannableString(texto)
        val inicio = texto.indexOf("Cadastre aqui")
        val fim = inicio + "Cadastre aqui".length
        spannable.setSpan(UnderlineSpan(), inicio, fim, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textCadastroUsuario.text = spannable

        // Clique no botão de login
        binding.btnLogin.setOnClickListener {
            val email = binding.inputLoginEmail.text.toString().trim()
            val senha = binding.inputLoginSenha.text.toString().trim()

        // Verificação dos campos preenchidos pelo usuário
            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                val loginValido = usuarioDAO.autenticar(email, senha)

                if (loginValido) {
                    Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AcervoActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Redirecionamento para a tela de cadastro do usuário
        binding.textCadastroUsuario.setOnClickListener {
            val intent = Intent(this, CadastrarUsuarioActivity::class.java)
            startActivity(intent)
        }
    }
}
