package com.example.foodstache
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.viewpager2.widget.ViewPager2
import com.example.foodstache.Fragments.AddTabPageAdapter
import com.google.android.material.tabs.TabLayout

class AddPicturesActivity : AppCompatActivity() {

    private lateinit var BackBtn : ImageButton
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pictures)

        BackBtn = findViewById(R.id.add_back_arrow_btn)
        BackBtn.setOnClickListener {
            // start LoginActivity
            startActivity(Intent(this@AddPicturesActivity, BottomNavigation::class.java))
        }

        val tablayout:TabLayout=findViewById(R.id.add_tab_layout)
        val viewpager:ViewPager2=findViewById(R.id.add_viewpager)

        val adapter:AddTabPageAdapter= AddTabPageAdapter(this, tablayout.tabCount)
        viewpager.adapter = adapter

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                tablayout.selectTab(tablayout.getTabAt(position))
            }
        })

        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager.currentItem= tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }
}