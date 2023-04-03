package com.example.foodstache
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageButton
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.foodstache.Fragments.AddTabPageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask

class AddPicturesActivity : AppCompatActivity() {

    private lateinit var BackBtn : ImageButton
    private lateinit var AddBtn : ImageButton

    private var myUrl =""
    private var ImageUri: Uri?= null
    private var StoragePostRef:StorageReference?=null
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

        StoragePostRef=FirebaseStorage.getInstance().reference.child("Posts")

        AddBtn = findViewById(R.id.add_check_btn)
        AddBtn.setOnClickListener { UploadPost() }


    }

    private fun UploadPost()
    {
        when
        {
            ImageUri==null -> Toast.makeText(this, "Please select Post first", Toast.LENGTH_LONG)
            else -> {
                val fileRef= StoragePostRef!!.child(System.currentTimeMillis().toString())
                var uploadTask:StorageTask<*>
                uploadTask=fileRef.putFile(ImageUri!!)
            }

        }
    }
}