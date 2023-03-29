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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO : change deprecated ProgressDialog to ProgressBar

const val RC_SIGN_IN = 100

class LoginActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient : GoogleSignInClient

    // views
    private lateinit var mEmailEt : TextInputEditText
    private lateinit var mPasswordEt : TextInputEditText
    private lateinit var mLoginBtn : Button
    private lateinit var mNotHaveAccountTv : TextView
    private lateinit var mRecoverPassTv : TextView
    private lateinit var mGoogleLoginBtn : SignInButton

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

        // before mAuth
        // configure google sign in
        var gso : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // in the onCreate() method, initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance()

        // init
        mEmailEt = findViewById(R.id.emailEt)
        mPasswordEt = findViewById(R.id.passwordEt)
        mLoginBtn = findViewById(R.id.loginBtn)
        mNotHaveAccountTv = findViewById(R.id.not_have_accountTv)
        mRecoverPassTv = findViewById(R.id.recoverPassTV)
        mGoogleLoginBtn = findViewById(R.id.googleLoginBtn)

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

        // handle google login btn click
        mGoogleLoginBtn.setOnClickListener {
            // begin google login process
            val signInIntent : Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // result return from launching the intent from GoogleSignInApi.getSignInIntent(...)
        if(requestCode == RC_SIGN_IN) {
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // google sign in was successful, authenticate with firebase
                val account : GoogleSignInAccount = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            }
            catch(e : ApiException) {
                // Google sign in failed, update UI appropriately
                Toast.makeText(this@LoginActivity, ""+e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account : GoogleSignInAccount) {
        val credential : AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    // Sign in success, update UI with the signed in user's information
                    val user: FirebaseUser = mAuth.currentUser!!

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

                    // show user email in toast
                    Toast.makeText(this@LoginActivity, ""+user.email, Toast.LENGTH_SHORT).show()
                    // go to profile activity after logged in
                    startActivity(Intent(this@LoginActivity, BottomNavigation::class.java))
                    finish()
                }
                else {
                    // if sign in failed, display a message to the user
                    Toast.makeText(this@LoginActivity, "Login failed...", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e : Exception ->
                // get and show error message
                Toast.makeText(this@LoginActivity, ""+e.message, Toast.LENGTH_SHORT).show()
            }
    }
}