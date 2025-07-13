package com.example.trabalhofinalmobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trabalhofinalmobile.R
import com.example.trabalhofinalmobile.database.UserDatabaseHelper
import com.example.trabalhofinalmobile.databinding.ActivityCadastrarUsuarioBinding


class CadastrarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarUsuarioBinding
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = UserDatabaseHelper(this)

        binding.btnCadastrarUsuario.setOnClickListener {
            val email = binding.inputCadastroEmail.text.toString().trim()
            val senha = binding.inputCadastroSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else if (dbHelper.verificarUsuarioExistente(email)) {
                Toast.makeText(this, "Este e-mail já está cadastrado!", Toast.LENGTH_SHORT).show()
            } else {
                val sucesso = dbHelper.inserirUsuario(email, senha)
                if (sucesso) {
                    Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                    binding.inputCadastroEmail.text?.clear()
                    binding.inputCadastroSenha.text?.clear()

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