package com.vpm.peliculasvpm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Firebase Auth
        auth = Firebase.auth

        // Vincular vistas
        emailEditText = findViewById(R.id.EmailAddress)
        passwordEditText = findViewById(R.id.Password)
        loginButton = findViewById(R.id.login)
    }

    fun login(view: View) {

        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Validaciones básicas
        if (email.isEmpty()) {
            emailEditText.error = "Ingrese su email"
            emailEditText.requestFocus()
            return
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Ingrese su contraseña"
            passwordEditText.requestFocus()
            return
        }

        // Autenticación con Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val user = task.result?.user

                    Toast.makeText(this, "Login exitoso: ${user?.email}", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, Home::class.java)
                    intent.putExtra("email", user?.email)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this, task.exception?.message ?: "Error desconocido", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()

        val usuarioActual = auth.currentUser

        if (usuarioActual != null) {
            Toast.makeText(this, "Usuario previamente autenticado: ${usuarioActual.email}", Toast.LENGTH_LONG).show()

            startActivity(Intent(this, Home::class.java))
            finish()
        }
    }
}
