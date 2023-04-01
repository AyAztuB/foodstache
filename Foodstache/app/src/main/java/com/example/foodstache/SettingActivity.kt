package com.example.foodstache

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.core.Context

class SettingActivity : AppCompatActivity() {

    private lateinit var BackBtn : ImageButton
    private lateinit var mLogoutBtn : RelativeLayout
    private lateinit var firebaseAuth: FirebaseAuth


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        mLogoutBtn = findViewById(R.id.settings_logout_btn)
        BackBtn = findViewById(R.id.back_arrow_btn)
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser == null) {
            startActivity(Intent(this@SettingActivity, MainActivity::class.java))
            finish()
        }

        BackBtn.setOnClickListener {
            // start LoginActivity
            startActivity(Intent(this@SettingActivity, ProfileActivity::class.java))
        }

        mLogoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this@SettingActivity, MainActivity::class.java))
            finish()
        }

        // Dark Mode
        var Switch = findViewById<Switch>(R.id.Switchbtn)

        // To know if the user's phone is in dark mode then enable the switch
        Switch.isChecked = resources.configuration.isNightModeActive

        // set the switch to listen on checked change
        Switch.setOnCheckedChangeListener { _, isChecked ->

            // if the button is checked enable dark mode
            // else enable light mode
            if (Switch.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Switch.text = "Dark Mode"
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Switch.text = "Light Mode"
            }
        }

    }

}