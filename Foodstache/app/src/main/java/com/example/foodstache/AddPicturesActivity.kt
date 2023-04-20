package com.example.foodstache
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.foodstache.Fragments.AddImageFragment
import com.example.foodstache.Fragments.AddTabPageAdapter
import com.example.foodstache.databinding.ActivityAddPicturesBinding
import com.example.foodstache.databinding.FragmentAddImageBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask

class AddPicturesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPicturesBinding
    private lateinit var binding2 : FragmentAddImageBinding



    private lateinit var BackBtn : ImageButton
    private lateinit var AddBtn : ImageButton
    private lateinit var db : DatabaseReference
    private lateinit var StoragePostRef:StorageReference
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pictures)

        binding2= FragmentAddImageBinding.inflate(layoutInflater)
        binding= ActivityAddPicturesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BackBtn = findViewById(R.id.add_back_arrow_btn)
        AddBtn = findViewById(R.id.add_check_btn)
        StoragePostRef=FirebaseStorage.getInstance().getReference("Uploads")
        db=FirebaseDatabase.getInstance().getReference("Uploads")

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


        AddBtn.setOnClickListener {
            UploadPost()
            startActivity(Intent(this@AddPicturesActivity, BottomNavigation::class.java))
        }


    }

    private fun UploadPost()
    {
        val description=binding2.addImageDescription.text.toString()
        val dbr=FirebaseDatabase.getInstance().reference
        val id=dbr.push().key
        val image= binding2.imageToAdd.imageAlpha
        db.child(id.toString()).setValue(image, description)
            .addOnSuccessListener {
                binding2.imageToAdd.setImageBitmap(null)
                binding2.addImageDescription.text.clear()
                Toast.makeText(this, "image uploaded", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "no image selected", Toast.LENGTH_SHORT).show()
            }
    }
}