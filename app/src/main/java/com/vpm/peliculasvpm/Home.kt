package com.vpm.peliculasvpm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import androidx.appcompat.widget.Toolbar


class Home : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val database = Firebase.database
    val myRef = database.getReference("peliculas")
    lateinit var peliculas: ArrayList<Peliculas>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        val extras = intent.extras
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        /*val singout= findViewById<Button>(R.id.btnSingout)
        val saludo = findViewById<TextView>(R.id.saludo)
        saludo.setText(extras?.getCharSequence("email").toString())
        singout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }*/
        // Read from the database
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                peliculas = ArrayList<Peliculas>()
                val value = snapshot.value
                Log.d("real-time-database", "Value is: " + value)
                snapshot.children.forEach{
                    unit->
                    var pelicula = Peliculas(unit.child("nombre").value.toString(),
                        unit.child("anio").value.toString(),
                            unit.child("genero").value.toString(),
                                unit.key.toString())
                            peliculas.add(pelicula)
                }
                llenarLista()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("real-time-database", "Failed to read value.", error.toException())
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logOut){
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun llenarLista(){
        val adaptador = PeliAdapter(this,peliculas)
        val lista = findViewById<ListView>(R.id.lista)
        lista.adapter=adaptador
    }
}