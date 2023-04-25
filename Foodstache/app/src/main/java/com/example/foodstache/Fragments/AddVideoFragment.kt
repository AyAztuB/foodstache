package com.example.foodstache.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.foodstache.BottomNavigation
import com.example.foodstache.R
import com.example.foodstache.databinding.FragmentAddImageBinding
import com.example.foodstache.databinding.FragmentAddVideoBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddVideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddVideoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentAddVideoBinding
    private var videoUri: Uri?= null
    private var storagePostRef: StorageReference?= null
    private var myUrl=""

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
        binding= FragmentAddVideoBinding.inflate(inflater, container, false)
        val root:View=binding.root
        storagePostRef= FirebaseStorage.getInstance().reference.child("Posts Videos")

        binding.browseBtnVideo.setOnClickListener {
            var myfileintent = Intent(Intent.ACTION_GET_CONTENT)
            myfileintent.setType("video/*")
            ActivityResultLauncher.launch(myfileintent)
            //startActivityForResult(intent, 5);
        }

        binding.videoUploadBtn.setOnClickListener {
            UploadVideo()
            startActivity(Intent(this@AddVideoFragment.context, BottomNavigation::class.java))
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
                videoUri=uri
                binding.videoToAdd.setVideoURI(videoUri)
                binding.videoToAdd.start()
                Toast.makeText(context, "Video Selected", Toast.LENGTH_SHORT).show()
            } catch (ex:Exception){
                Toast.makeText(context, ex.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun UploadVideo(){
        when{
            videoUri==null->Toast.makeText(context, "please select an image first", Toast.LENGTH_LONG).show()

            else->{
                val progressDialog= ProgressDialog(this.context)
                progressDialog.setTitle("New video")
                progressDialog.setMessage("Please wait, we are adding your video")
                progressDialog.show()

                val fileref=storagePostRef!!.child(System.currentTimeMillis().toString()+".mp4")
                var uploadTask: StorageTask<*>
                uploadTask=fileref.putFile(videoUri!!)

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task->
                    if (!task.isSuccessful){
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileref.downloadUrl
                }).addOnCompleteListener(OnCompleteListener<Uri>{ task ->
                    if (task.isSuccessful){
                        val downloadUrl=task.result
                        myUrl=downloadUrl.toString()

                        val ref= FirebaseDatabase.getInstance().reference.child("Video")
                        val postId= ref.push().key

                        val userMap= HashMap<String, Any>()

                        userMap["postId"]=postId!!
                        userMap["description"]=binding.addVideoDescription.text.toString().toLowerCase()
                        userMap["user"]= FirebaseAuth.getInstance().currentUser!!.uid
                        userMap["Image"]=myUrl

                        ref.child(postId).updateChildren(userMap)

                        Toast.makeText(context, "image uploaded", Toast.LENGTH_SHORT).show()

                        val intent=Intent(this@AddVideoFragment.context, BottomNavigation::class.java)
                        startActivity(intent)
                        progressDialog.dismiss()
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
         * @return A new instance of fragment AddVideoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddVideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}