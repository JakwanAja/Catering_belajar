package com.projectuas.topupgameapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.projectuas.topupgameapp.databinding.ActivityLoginBinding
import com.projectuas.topupgameapp.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val emailOrUsername = binding.inputUser.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            if (emailOrUsername.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email/Username dan Password wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Asumsikan input adalah email (Firebase login pakai email)
            firebaseAuth.signInWithEmailAndPassword(emailOrUsername, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
