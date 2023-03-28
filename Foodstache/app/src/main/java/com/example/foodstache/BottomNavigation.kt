package com.example.foodstache

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//import com.example.foodstache.databinding.ActivityBottomNavigationBinding

class BottomNavigation : AppCompatActivity() {

    //private lateinit var binding: ActivityBottomNavigationBinding
    private lateinit var textView: TextView

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                textView.setText("Home")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_videos-> {
                textView.setText("Video")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_add -> {
                textView.setText("Add")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_recette -> {
                textView.setText("Recette")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_chat -> {
                textView.setText("Chat")
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        //binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        // init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        textView = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

       /* val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_navigation)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/
    }

    private fun checkUserStatus() {
        // get current user
        val user : FirebaseUser? = firebaseAuth.currentUser
        if(user == null) {
            // user not signed in, go to MainActivity
            startActivity(Intent(this@BottomNavigation, MainActivity::class.java))
            finish()
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