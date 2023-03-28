package com.example.foodstache

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.*
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
    private lateinit var mRecoverPassTv : TextView

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
        mRecoverPassTv = findViewById(R.id.recoverPassTV)

        // init progressDialog
        progressDialog = ProgressDialog(this)

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
            finish()
        }

        // recover password TextView click
        mRecoverPassTv.setOnClickListener {
            showRecoverPasswordDialog()
        }
    }

    private fun showRecoverPasswordDialog() {
        // AlertDialog
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Recover Password")

        // set layout linear layout
        val linearLayout : LinearLayout = LinearLayout(this)

        // views to set in dialog
        val emailEt : EditText = EditText(this)
        emailEt.hint = "Email"
        emailEt.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        // sets the min width of a EditView to fit a text of n 'M' letters regardless of the actual text extension and text size
        emailEt.minEms = 16

        linearLayout.addView(emailEt)
        linearLayout.setPadding(10,10,10,10)

        builder.setView(linearLayout)

        // button recover
        builder.setPositiveButton("Recover") { _, _ ->
            // input email
            val email : String = emailEt.text.toString().trim()
            beginRecovery(email)
        }

        // button cancel
        builder.setNegativeButton("Cancel") { dialog, _ ->
            // dismiss dialog
            dialog.dismiss()
        }

        // show dialog
        builder.create().show()
    }

    private fun beginRecovery(email : String) {
        // show progressDialog
        progressDialog.setMessage("Sending email...")
        progressDialog.show()
        if (email == "") {
            progressDialog.dismiss()
            Toast.makeText(this@LoginActivity, "Failed...", Toast.LENGTH_SHORT).show()
            return
        }
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener {task ->
            progressDialog.dismiss()
            if(task.isSuccessful) {
                Toast.makeText(this@LoginActivity, "Email sent", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this@LoginActivity, "Failed...", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {e ->
            progressDialog.dismiss()
            // get and show proper error message
            Toast.makeText(this@LoginActivity, ""+e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginUser(email : String, password : String) {
        // show progressDialog
        progressDialog.setMessage("Logging in...")
        progressDialog.show()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    // dismiss progressDialog
                    progressDialog.dismiss()
                    // Sign in success
                    // val user : FirebaseUser = mAuth.currentUser!! // UNUSED
                    // user is logged in, start ProfileActivity
                    startActivity(Intent(this@LoginActivity, BottomNavigation::class.java))
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