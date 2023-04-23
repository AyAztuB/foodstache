package com.example.foodstache

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterProfilActivity : AppCompatActivity() {

    private lateinit var editBtn : Button
    private lateinit var nameProfil : TextInputEditText
    private lateinit var userName : TextInputEditText
    private lateinit var phoneNumber : TextInputEditText
    private lateinit var bioProfil : TextInputEditText
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_profil)

        editBtn = findViewById(R.id.validateBtn)
        nameProfil = findViewById(R.id.nomProfiltx)
        userName = findViewById(R.id.usernametx)
        phoneNumber = findViewById(R.id.phonenumberTx)
        bioProfil = findViewById(R.id.bioTx)
        database = Firebase.database.reference


        editBtn.setOnClickListener{
            val userId = intent.getStringExtra("userId")
            val name : String = nameProfil.text.toString().trim()
            val username : String = userName.text.toString().trim()
            val phone : String = phoneNumber.text.toString().trim()
            val bio : String = bioProfil.text.toString().trim()

            if (userId != null) {
                editProfil(name, username, phone, bio, userId)
            }

        }

    }
    private fun editProfil(name : String, username : String, phone : String, bio : String, userID : String) {
        if(username.isNotEmpty()) {
            database.child("Users").child(userID).child("username").setValue(username)
        }
        if(name.isNotEmpty()) {
            database.child("Users").child(userID).child("name").setValue(name)
        }
        if(phone.isNotEmpty()) {
            database.child("Users").child(userID).child("phone").setValue(phone)
        }
        if(bio.isNotEmpty()) {
            database.child("Users").child(userID).child("bio").setValue(bio)
        }
        startActivity(Intent(this@RegisterProfilActivity, BottomNavigation::class.java))

    }
}