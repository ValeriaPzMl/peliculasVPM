package com.vpm.peliculasvpm

import android.content.Intent
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth



    }
    fun login(view: View){
        auth.signInWithEmailAndPassword("vpmf27@gmail.com","contrasena1234").addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this,"login exitoso",Toast.LENGTH_LONG).show()
                startActivity(Intent(this,Home::class.java).putExtra("email",task.result.user?.email.toString()))
            }else{
                Toast.makeText(this, task.exception?.message.toString(),Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onStart() {
        super.onStart()

        val usuarioActual = Firebase.auth.currentUser
        if(usuarioActual != null){
            Toast.makeText(this,"usuario preiamente autenticado",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"no hay usuario",Toast.LENGTH_LONG).show()

        }
    }
}