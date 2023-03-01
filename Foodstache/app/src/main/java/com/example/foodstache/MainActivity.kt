package com.example.foodstache

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    // views
    private lateinit var mRegisterBtn : Button
    private lateinit var mLoginBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init views
        mRegisterBtn = findViewById(R.id.register_btn)
        mLoginBtn = findViewById(R.id.login_btn)

        // handle register button click
        mRegisterBtn.setOnClickListener{
            // start RegisterActivity
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        }

        // handle login button click
        mLoginBtn.setOnClickListener {
            // start LoginActivity
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }
}