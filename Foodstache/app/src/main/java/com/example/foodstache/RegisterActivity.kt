package com.example.foodstache

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO : change deprecated ProgressDialog to ProgressBar

class RegisterActivity : AppCompatActivity() {

    // views
    private lateinit var mEmailEt : TextInputEditText
    private lateinit var mPasswordEt : TextInputEditText
    private lateinit var mRegisterBtn : Button
    private lateinit var mHaveAccountTv : TextView

    // progressbar to display while registering user
    private lateinit var progressDialog : ProgressDialog

    // Declare an instance of FirebaseAuth
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // ActionBar and its title
        val actionBar : ActionBar = supportActionBar!!
        actionBar.title = "Create Account"
        // Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // init
        mEmailEt = findViewById(R.id.emailEt)
        mPasswordEt = findViewById(R.id.passwordEt)
        mRegisterBtn = findViewById(R.id.registerBtn)
        mHaveAccountTv = findViewById(R.id.have_accountTv)

        // In the onCreate() methode, initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Registering user...")

        // handle register btn click
        mRegisterBtn.setOnClickListener{
            // input email, password
            val email : String = mEmailEt.text.toString().trim()
            val password : String = mPasswordEt.text.toString().trim()
            // validate
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // set error and focus on email edittext
                mEmailEt.error = "Invalid Email"
                mEmailEt.isFocusable = true
            }
            else if(password.length < 6) {
                // set error and focus on password edittext
                mPasswordEt.error = "Password needs at least 6 characters"
                mPasswordEt.isFocusable = true
            }
            else {
                registerUser(email, password) // register the user
            }
        }

        // handle login TextView click listener
        mHaveAccountTv.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser(email : String, password : String) {
        // email and password pattern is valid, show progress dialog and start registering user
        progressDialog.show()

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    // Sign in success, dismiss dialog and start profile activity
                    progressDialog.dismiss()
                    val user : FirebaseUser = mAuth.currentUser!!

                    // get user email and uid from auth
                    val _email : String = user.email!!
                    val uid : String = user.uid
                    // when user is registered store info in firebase realtime database too using HashMap
                    val hashMap : HashMap<String, String> = HashMap()
                    // put info in hashMap
                    hashMap["email"] = _email
                    hashMap["uid"] = uid
                    hashMap["name"] = "" // Will add later (e.g. edit profile)
                    hashMap["phone"] = "" // Will add later (e.g. edit profile)
                    hashMap["image"] = "" // Will add later (e.g. edit profile)
                    // firebase database instance
                    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
                    // path to store user data named "Users"
                    val reference : DatabaseReference = database.getReference("Users")
                    // put data within hashmap in database
                    reference.child(uid).setValue(hashMap)

                    Toast.makeText(this@RegisterActivity, "Registered...\n"+user.email, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, BottomNavigation::class.java))
                    finish()
                }
                else {
                    // If sign in fails, display a message to the user
                    progressDialog.dismiss()
                    Toast.makeText(this@RegisterActivity, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                // error, dismiss progress dialog and get&show the error message
                progressDialog.dismiss()
                Toast.makeText(this@RegisterActivity, ""+e.message, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        // onBackPressed() // DEPRECATED
        finish() // go to previous activity
        return super.onSupportNavigateUp()
    }
}