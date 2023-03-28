package com.example.foodstache

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodstache.Fragments.ChatFragment
import com.example.foodstache.Fragments.HomeFragment
import com.example.foodstache.Fragments.RecetteFragment
import com.example.foodstache.Fragments.Video1Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//import com.example.foodstache.databinding.ActivityBottomNavigationBinding

class BottomNavigation : AppCompatActivity() {

    internal var selectedFragment: Fragment? = null

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                selectedFragment=HomeFragment()
            }
            R.id.nav_videos-> {
                selectedFragment=Video1Fragment()
            }
            R.id.nav_add -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_recette -> {
                selectedFragment=RecetteFragment()
            }
            R.id.nav_chat -> {
                selectedFragment=ChatFragment()
            }
        }

        if (selectedFragment!=null)
        {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
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
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
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