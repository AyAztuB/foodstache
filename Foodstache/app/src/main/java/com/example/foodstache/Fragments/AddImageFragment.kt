package com.example.foodstache.Fragments

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.foodstache.BottomNavigation
import com.example.foodstache.R
import com.example.foodstache.databinding.FragmentAddImageBinding
import com.example.foodstache.databinding.FragmentHomeBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentAddImageBinding

    private var myUrl=""
    private var imageUri: Uri?= null
    private var storagePostRef:StorageReference?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddImageBinding.inflate(inflater, container, false)
        val root:View=binding.root
        storagePostRef=FirebaseStorage.getInstance().reference.child("Posts Images")

        binding.browseBtnImage.setOnClickListener(){
            var myfileintent = Intent(Intent.ACTION_GET_CONTENT)
            myfileintent.setType("image/*")
            ActivityResultLauncher.launch(myfileintent)
        }

        binding.imageUploadBtn.setOnClickListener {
            UploadPost()
        }

        // Inflate the layout for this fragment
        return root
    }

    private val ActivityResultLauncher= registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){result:ActivityResult ->
        if (result.resultCode==AppCompatActivity.RESULT_OK)
        {
            val uri= result.data!!.data
            try{
                imageUri=uri
                binding.imageToAdd.setImageURI(imageUri)
                Toast.makeText(context, "Image Selected", Toast.LENGTH_SHORT).show()
            } catch (ex:Exception){
                Toast.makeText(context, ex.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun UploadPost()
    {
        when{
            imageUri==null->Toast.makeText(context, "please select an image first", Toast.LENGTH_LONG).show()

            else->{
                val progressDialog= ProgressDialog(this.context)
                progressDialog.setTitle("New image")
                progressDialog.setMessage("Please wait, we are adding your image")
                progressDialog.show()

                val fileref=storagePostRef!!.child(System.currentTimeMillis().toString()+".jpg")
                var uploadTask:StorageTask<*>
                uploadTask=fileref.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task->
                    if (!task.isSuccessful){
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileref.downloadUrl
                }).addOnCompleteListener(OnCompleteListener<Uri>{task ->
                    if (task.isSuccessful){
                        val downloadUrl=task.result
                        myUrl=downloadUrl.toString()

                        val ref= FirebaseDatabase.getInstance().reference.child("Image")
                        val postId= ref.push().key

                        val userMap= HashMap<String, Any>()

                        val user = FirebaseAuth.getInstance().currentUser!!

                        userMap["postID"]=postId!!
                        userMap["description"]=binding.addImageDescription.text.toString().toLowerCase()
                        userMap["userID"]= user.uid
                        userMap["Image"]=myUrl
                        userMap["Time"]= (System.currentTimeMillis()*-1).toString()
                        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                        val query : Query = databaseReference.orderByChild("uid").equalTo(user.uid)
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (ds in snapshot.children) {
                                    userMap["Username"] = "" + ds.child("username").value
                                    userMap["userPP"] = ""+ds.child("image").value

                                    // change nbPost value
                                    val nbPost = ds.child("nbPost").value.toString().toInt()
                                    val newNbPost = nbPost + 1
                                    ds.child("nbPost").ref.setValue(newNbPost.toString())

                                    // Post a post :)
                                    ref.child(postId).updateChildren(userMap)

                                    Toast.makeText(context, "image uploaded", Toast.LENGTH_SHORT).show()

                                    val intent=Intent(this@AddImageFragment.context, BottomNavigation::class.java)
                                    startActivity(intent)
                                    progressDialog.dismiss()

                                    break
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(activity, ""+error.message, Toast.LENGTH_SHORT).show()

                                val intent=Intent(this@AddImageFragment.context, BottomNavigation::class.java)
                                startActivity(intent)
                                progressDialog.dismiss()
                            }
                        })
                    }
                })
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddImageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}