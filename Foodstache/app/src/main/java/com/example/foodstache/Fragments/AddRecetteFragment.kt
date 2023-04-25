package com.example.foodstache.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.foodstache.BottomNavigation
import com.example.foodstache.R
import com.example.foodstache.databinding.FragmentAddImageBinding
import com.example.foodstache.databinding.FragmentAddRecetteBinding
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
 * Use the [AddRecetteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddRecetteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentAddRecetteBinding
    private var storagePostRef: StorageReference?= null
    private lateinit var TextLayout:View

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
        binding= FragmentAddRecetteBinding.inflate(inflater, container, false)
        val root:View=binding.root
        storagePostRef= FirebaseStorage.getInstance().reference.child("Posts Recettes")
        TextLayout=getLayoutInflater().inflate(R.layout.add_edittext, null, false)
        val userMap= HashMap<String, Any>()

        binding.addRecetteIngredient.setOnClickListener {
            var lview=getLayoutInflater().inflate(R.layout.add_edittext, null, false)
            var supp=lview.findViewById<ImageButton>(R.id.supp_ET)
            supp.visibility=View.VISIBLE

            supp.setOnClickListener {
                binding.list1.removeView(lview)
            }
            binding.list1.addView(lview)
        }

        binding.addRecetteEtape.setOnClickListener {
            var lview=getLayoutInflater().inflate(R.layout.add_edittext, null, false)
            var supp=lview.findViewById<ImageButton>(R.id.supp_ET)
            supp.visibility=View.VISIBLE

            supp.setOnClickListener {
                binding.list2.removeView(lview)
            }
            binding.list2.addView(lview)

        }

        binding.recetteUploadBtn.setOnClickListener {
            when{
                binding.addRecetteTitle.text.toString()==""-> Toast.makeText(context, "Title is missing", Toast.LENGTH_LONG).show()
                TextLayout.findViewById<EditText>(R.id.add_recette_ingredient_ET).toString()==""->Toast.makeText(context, "no text inserted", Toast.LENGTH_LONG).show()

                else->{
                    val progressDialog= ProgressDialog(this.context)
                    progressDialog.setTitle("New Recette")
                    progressDialog.setMessage("Please wait, we are adding your Recipe")
                    progressDialog.show()

                    val ref= FirebaseDatabase.getInstance().reference.child("Recette")
                    val postId= ref.push().key

                    userMap["postid"]=postId!!
                    userMap["user"]= FirebaseAuth.getInstance().currentUser!!.uid
                    userMap["Titre"]=binding.addRecetteTitle.text.toString()

                    for (text in 0 until binding.list1.childCount)
                        userMap["ingredient $text"]=binding.list1.getChildAt(text).findViewById<EditText>(R.id.add_recette_ingredient_ET).text.toString().toLowerCase()

                    for (text in 0 until binding.list2.childCount)
                        userMap["Etape $text"]=binding.list2.getChildAt(text).findViewById<EditText>(R.id.add_recette_ingredient_ET).text.toString().toLowerCase()

                    ref.child(postId).updateChildren(userMap)

                    Toast.makeText(context, "recipe uploaded", Toast.LENGTH_SHORT).show()

                    val intent= Intent(this@AddRecetteFragment.context, BottomNavigation::class.java)
                    startActivity(intent)
                    progressDialog.dismiss()
                }
            }
        }
        // Inflate the layout for this fragment
        return root
    }

    private fun UploadPost()
    {

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddRecetteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddRecetteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}