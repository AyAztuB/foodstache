package com.example.foodstache

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class SettingActivity : AppCompatActivity() {

    private lateinit var BackBtn : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        BackBtn = findViewById(R.id.back_arrow_btn)

        BackBtn.setOnClickListener {
            // start LoginActivity
            startActivity(Intent(this@SettingActivity, ProfileActivity::class.java))
        }
    }
}