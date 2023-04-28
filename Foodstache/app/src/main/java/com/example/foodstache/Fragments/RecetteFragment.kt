package com.example.foodstache.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstache.Adapter.AdapterPicturePosts
import com.example.foodstache.Adapter.AdapterRecipePosts
import com.example.foodstache.Model.ModelPicturePost
import com.example.foodstache.Model.ModelRecipePost
import com.example.foodstache.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecetteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecetteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var recipePostList: ArrayList<ModelRecipePost>
    private lateinit var adapterRecipePost: AdapterRecipePosts

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
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_recette, container, false)

        // recycler view and its properties
        recyclerView = view.findViewById(R.id.recycler_view_recipe)
        val layoutManager : LinearLayoutManager = LinearLayoutManager(context)
        // show newest post first
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager

        recipePostList = ArrayList()
        loadRecipePosts()

        return view
    }

    private fun loadRecipePosts() {
        val followingUID = ArrayList<String>()
        val refFollowing : DatabaseReference = FirebaseDatabase.getInstance().getReference("Follow").child(
            FirebaseAuth.getInstance().currentUser!!.uid).child("Following")
        refFollowing.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    followingUID.add(ds.key!!)
                }

                val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Data").child("Recipe")
                val query: Query = ref.orderByChild("Time")
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        recipePostList.clear()
                        for(ds in snapshot.children) {
                            val userID : String = ds.child("userID").value.toString()

                            if (followingUID.contains(userID)) {

                                val nbIngredients : Int = ds.child("nbIngredients").value.toString().toInt()
                                val nbSteps : Int = ds.child("nbSteps").value.toString().toInt()
                                val Ingredients : ArrayList<String> = ArrayList()
                                val Steps : ArrayList<String> = ArrayList()
                                for(i in 0 until nbIngredients) {
                                    Ingredients.add(ds.child("ingredient $i").value.toString())
                                }
                                for(i in 0 until nbSteps) {
                                    Steps.add(ds.child("step $i").value.toString())
                                }
                                val modelPost : ModelRecipePost = ModelRecipePost(
                                    ds.child("userID").value.toString(),
                                    ds.child("title").value.toString(),
                                    ds.child("Time").value.toString(),
                                    ds.child("Username").value.toString(),
                                    ds.child("userPP").value.toString(), Ingredients, Steps)

                                recipePostList.add(modelPost)
                                adapterRecipePost = AdapterRecipePosts(context!!, recipePostList)
                                recyclerView.adapter = adapterRecipePost
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, ""+error.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, ""+databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecetteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecetteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}