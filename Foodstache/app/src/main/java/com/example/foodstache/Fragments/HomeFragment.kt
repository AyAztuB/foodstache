package com.example.foodstache.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstache.Adapter.AdapterPicturePosts
import com.example.foodstache.BottomNavigation
import com.example.foodstache.LoginActivity
import com.example.foodstache.Model.ModelPicturePost
import com.example.foodstache.ProfileActivity
import com.example.foodstache.R
import com.example.foodstache.databinding.FragmentAddImageBinding
import com.example.foodstache.databinding.FragmentHome2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var db : DatabaseReference
    private lateinit var binding : FragmentHome2Binding

    private lateinit var recyclerView: RecyclerView
    private lateinit var picturePostList: ArrayList<ModelPicturePost>
    private lateinit var adapterPicturePost: AdapterPicturePosts

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        binding= FragmentHome2Binding.inflate(inflater, container, false)
        val root=binding.root
        db= FirebaseDatabase.getInstance().getReference("Uploads")
        binding.imageProfileBtn.setOnClickListener {
            startActivity(Intent(this@HomeFragment.context, ProfileActivity::class.java))
        }
       /* var query=db.orderByChild("image")
        query.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var ky:String=""
                    for (itmsnapshot in snapshot.children){
                        ky=itmsnapshot.key.toString()
                    }
                }
            }
        })*/

        // WHY 2 TIMES THE SAME ???
        /*
        binding.imageProfileBtn.setOnClickListener {
            startActivity(Intent(this@HomeFragment.context, ProfileActivity::class.java))
        }
        */

        // recycler view and its properties
        recyclerView = root.findViewById(R.id.recycler_view_home)
        val layoutManager : LinearLayoutManager = LinearLayoutManager(context)
        // show newest post first
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager

        picturePostList = ArrayList()
        loadPicturePosts()

        // Inflate the layout for this fragment
        return root
    }

    private fun loadPicturePosts() {
        val followingUID = ArrayList<String>()
        val refFollowing : DatabaseReference = FirebaseDatabase.getInstance().getReference("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Following")
        refFollowing.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    followingUID.add(ds.key!!)
                }

                val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Image")
                val query: Query = ref.orderByChild("Time")
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        picturePostList.clear()
                        for(ds in snapshot.children) {
                            val modelPost : ModelPicturePost = ds.getValue(ModelPicturePost::class.java) as ModelPicturePost
                            if (followingUID.contains(modelPost.userID)) {
                                picturePostList.add(modelPost)
                                adapterPicturePost = AdapterPicturePosts(context!!, picturePostList)
                                recyclerView.adapter = adapterPicturePost
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(activity, ""+error.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, ""+databaseError.message, Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}