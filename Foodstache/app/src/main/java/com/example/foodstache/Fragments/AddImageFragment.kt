package com.example.foodstache.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
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
import com.example.foodstache.R
import com.example.foodstache.databinding.FragmentAddImageBinding
import com.example.foodstache.databinding.FragmentHomeBinding
import com.google.firebase.database.DatabaseReference
import java.io.ByteArrayOutputStream

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
    var sImage:String?=""



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
        binding.browseBtnImage.setOnClickListener(){
            var myfileintent = Intent(Intent.ACTION_GET_CONTENT)
            myfileintent.setType("image/*")
            ActivityResultLauncher.launch(myfileintent)
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
                val inputStream= context?.contentResolver?.openInputStream(uri!!)
                val myBitmap= BitmapFactory.decodeStream(inputStream)
                val stream= ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes= stream.toByteArray()
                sImage= Base64.encodeToString(bytes,Base64.DEFAULT)
                binding.imageToAdd.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(context, "Image Selected", Toast.LENGTH_SHORT).show()
            } catch (ex:Exception){
                Toast.makeText(context, ex.message.toString(), Toast.LENGTH_LONG).show()
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