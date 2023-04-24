package com.example.foodstache

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodstache.databinding.ActivityRegisterProfilBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.util.*


class RegisterProfilActivity : AppCompatActivity() {

    private lateinit var editBtn : Button
    private lateinit var uploadBtn : Button
    private lateinit var cameraBtn : Button
    private lateinit var nameProfil : TextInputEditText
    private lateinit var userName : TextInputEditText
    private lateinit var phoneNumber : TextInputEditText
    private lateinit var bioProfil : TextInputEditText
    private lateinit var database: DatabaseReference
    private lateinit var storageRef : StorageReference
    private lateinit var mAvatarView : ImageView
    private var storagePostRef:StorageReference?= null
    private lateinit var binding : ActivityRegisterProfilBinding

    private val PICK_IMAGE_REQUEST = 71
    private var myUrl: Uri? =null
    private lateinit var imageLink : String
    var pictureProfile: Boolean = false
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_profil)

        editBtn = findViewById(R.id.validateBtn)
        cameraBtn = findViewById(R.id.camera_btn_profil)
        uploadBtn = findViewById(R.id.browse_btn_image_profil)
        nameProfil = findViewById(R.id.nomProfiltx)
        userName = findViewById(R.id.usernametx)
        phoneNumber = findViewById(R.id.phonenumberTx)
        bioProfil = findViewById(R.id.bioTx)
        mAvatarView = findViewById(R.id.image_to_add)
        database = Firebase.database.reference
        storageRef = Firebase.storage.reference



        editBtn.setOnClickListener{
            val userId = intent.getStringExtra("userId")
            val name : String = nameProfil.text.toString().trim()
            val username : String = userName.text.toString().trim()
            val phone : String = phoneNumber.text.toString().trim()
            val bio : String = bioProfil.text.toString().trim()

            if (userId != null) {
                editProfil(name, username, phone, bio, userId)
            }

        }

        uploadBtn.setOnClickListener{
            pictureProfile = true
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), 22)
        }

        cameraBtn.setOnClickListener {
            val cameraIntent = Intent()
            cameraIntent.type="image/*"
            cameraIntent.action=MediaStore.ACTION_IMAGE_CAPTURE
            startActivityForResult(Intent.createChooser(cameraIntent, "Test"), 22)

        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // on below line we are checking if the result is ok
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.data != null) {
            // on below line initializing file uri with the data which we get from intent
            myUrl = data.data
            try {
                Picasso.get().load(myUrl).into(mAvatarView)
            } catch (e: Exception) {
                // handling exception on below line
                e.printStackTrace()
            }
        }
    }
    fun uploadImage() {
        // on below line checking weather our file uri is null or not.
        if (myUrl != null) {
            // on below line displaying a progress dialog when uploading an image.
            val progressDialog = ProgressDialog(this)
            // on below line setting title and message for our progress dialog and displaying our progress dialog.
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()

            // on below line creating a storage refrence for firebase storage and creating a child in it with
            // random uuid.
            val ref: StorageReference = FirebaseStorage.getInstance().reference.child("Profil Pictures")
                .child(UUID.randomUUID().toString())
            // on below line adding a file to our storage.
            ref.putFile(myUrl!!).addOnSuccessListener { it ->
                // this method is called when file is uploaded.
                // in this case we are dismissing our progress dialog and displaying a toast message
                val result = it.metadata!!.reference!!.downloadUrl;
                result.addOnSuccessListener {
                    imageLink = it.toString()
                }
                val userId = intent.getStringExtra("userId")
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                // this method is called when there is failure in file upload.
                // in this case we are dismissing the dialog and displaying toast message
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Fail to Upload Image..", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun editProfil(name : String, username : String, phone : String, bio : String, userID : String) {
        if(username.isNotEmpty()) {
            database.child("Users").child(userID).child("username").setValue(username)
        }
        if(name.isNotEmpty()) {
            database.child("Users").child(userID).child("name").setValue(name)
        }
        if(phone.isNotEmpty()) {
            database.child("Users").child(userID).child("phone").setValue(phone)
        }
        if(bio.isNotEmpty()) {
            database.child("Users").child(userID).child("bio").setValue(bio)
        }
        if(pictureProfile) {
            uploadImage()
            database.child("Users").child(userID).child("image").setValue(imageLink)

        }
        startActivity(Intent(this@RegisterProfilActivity, BottomNavigation::class.java))

    }







}