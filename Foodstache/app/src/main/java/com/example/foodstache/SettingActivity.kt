package com.example.foodstache

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SettingActivity : AppCompatActivity() {

    private lateinit var BackBtn : ImageButton
    private lateinit var mLogoutBtn : RelativeLayout
    private lateinit var firebaseAuth: FirebaseAuth

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
    }
}