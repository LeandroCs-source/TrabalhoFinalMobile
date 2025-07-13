package com.example.trabalhofinalmobile.main

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalhofinalmobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val texto = "Primeiro acesso? Cadastre aqui"
        val spannable = SpannableString(texto)

        val inicio = texto.indexOf("Cadastre aqui")
        val fim = inicio + "Cadastre aqui".length

        spannable.setSpan(
            UnderlineSpan(),
            inicio,
            fim,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.textCadastroUsuario.text = spannable

        binding.btnLogin.setOnClickListener {
            val email = binding.textEmailLogin.text.toString().trim()
            val senha = binding.textSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else if (email == "admin@livros.com" && senha == "1234") {
                Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.textCadastroUsuario.setOnClickListener{
            val intent = Intent(this, CadastrarUsuarioActivity::class.java)
            startActivity(intent)
        }
    }
}