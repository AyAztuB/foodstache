package com.example.foodstache

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileActivity : AppCompatActivity() {

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    // views
    private lateinit var mProfileTv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // ActionBar and its title
        val actionBar : ActionBar = supportActionBar!!
        actionBar.title = "Profile"

        // init
        firebaseAuth = FirebaseAuth.getInstance()

        // init views
        mProfileTv = findViewById(R.id.profileTv)
    }

    private fun checkUserStatus() {
        // get current user
        val user : FirebaseUser? = firebaseAuth.currentUser
        if(user == null) {
            // user not signed in, go to MainActivity
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            finish()
        }
        else {
            // user is signed in, stay here
            // set email of logged in user
            mProfileTv.text = user.email
        }
    }

    override fun onStart() {
        // check on start of app
        checkUserStatus()
        super.onStart()
    }

    /* inflate options menu */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // inflating enu
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /* handle menu item clicks */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // get item id
        val id : Int = item.itemId
        if (id == R.id.action_logout) {
            firebaseAuth.signOut()
            checkUserStatus()
        }
        return super.onOptionsItemSelected(item)
    }
}