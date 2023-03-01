package com.example.foodstache

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

// TODO : change deprecated ProgressDialog to ProgressBar

class LoginActivity : AppCompatActivity() {

    // views
    private lateinit var mEmailEt : TextInputEditText
    private lateinit var mPasswordEt : TextInputEditText
    private lateinit var mLoginBtn : Button
    private lateinit var mNotHaveAccountTv : TextView

    // declare an instance of FirebaseAuth
    private lateinit var mAuth : FirebaseAuth

    // progress dialog
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // ActionBar and its title
        val actionBar : ActionBar = supportActionBar!!
        actionBar.title = "Login"
        // Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // in the onCreate() method, initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance()

        // init
        mEmailEt = findViewById(R.id.emailEt)
        mPasswordEt = findViewById(R.id.passwordEt)
        mLoginBtn = findViewById(R.id.loginBtn)
        mNotHaveAccountTv = findViewById(R.id.not_have_accountTv)

        // init progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Logging in...")

        // login button click
        mLoginBtn.setOnClickListener {
            // input data
            val email : String = mEmailEt.text.toString()
            val password : String = mPasswordEt.text.toString().trim()
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // invalid pattern email, set error
                mEmailEt.error = "Invalid Email"
                mEmailEt.isFocusable = true
            }
            else {
                // valid email pattern
                loginUser(email, password)
            }
        }

        // not have account TextView click
        mNotHaveAccountTv.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun loginUser(email : String, password : String) {
        // show progressDialog
        progressDialog.show()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    // dismiss progressDialog
                    progressDialog.dismiss()
                    // Sign in success
                    // val user : FirebaseUser = mAuth.currentUser!! // UNUSED
                    // user is logged in, start ProfileActivity
                    startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
                    finish()
                }
                else {
                    // dismiss progressDialog
                    progressDialog.dismiss()
                    // if sign in fails, display a message to the user
                    Toast.makeText(this@LoginActivity, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                // dismiss progressDialog
                progressDialog.dismiss()
                // error, get and show error message
                Toast.makeText(this@LoginActivity, ""+e.message, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        // onBackPressed() // DEPRECATED
        finish() // go to previous activity
        return super.onSupportNavigateUp()
    }
}