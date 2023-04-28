package com.example.foodstache.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstache.Adapter.*
import com.example.foodstache.Model.*
import com.example.foodstache.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoFragment : Fragment() {


    private var recyclerView: RecyclerView? = null
    private var userAdapter : UserAdapter? = null
    private lateinit var search_text : EditText
    private var mUser : MutableList<User>? = null

    private lateinit var recyclerViewVideo: RecyclerView
    private lateinit var videoPostList: ArrayList<ModelPicturePost>
    private lateinit var adapterVideoPost: AdapterVideoPosts

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_video, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_search)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        mUser = ArrayList()
        userAdapter = context?.let{UserAdapter(it, mUser as ArrayList<User>, true)}
        recyclerView?.adapter = userAdapter
        search_text = view.findViewById(R.id.search_view_edit_text)


        search_text.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(search_text.text.toString()==""){

                }
                else{
                    recyclerView?.visibility= View.VISIBLE
                    retrieveUser()
                    searchUser(p0.toString().toLowerCase())
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        // recycler view and its properties
        recyclerViewVideo = view.findViewById(R.id.recycler_view_video)
        val layoutManager : LinearLayoutManager = LinearLayoutManager(context)
        // show newest post first
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerViewVideo.layoutManager = layoutManager

        videoPostList = ArrayList()
        loadVideoPosts()

        return view
    }

    private fun searchUser(input: String) {
        val query = FirebaseDatabase.getInstance().getReference()
            .child("Users").orderByChild("name").startAt(input).endAt(input + "\uf8ff")

        query.addValueEventListener(object  : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUser?.clear()

                for(snapshot in dataSnapshot.children){
                    val user = snapshot.getValue(User::class.java)
                    if(user != null){
                        mUser?.add(user)
                    }
                }
                userAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun retrieveUser() {
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users")
        usersRef.addValueEventListener(object  : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(search_text.text.toString()==""){
                    mUser?.clear()

                    for(snapshot in dataSnapshot.children){
                        val user = snapshot.getValue(User::class.java)
                        if(user != null){
                            mUser?.add(user)
                        }
                    }
                    userAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun loadVideoPosts() {
        val followingUID = ArrayList<String>()
        val refFollowing : DatabaseReference = FirebaseDatabase.getInstance().getReference("Follow").child(
            FirebaseAuth.getInstance().currentUser!!.uid).child("Following")
        refFollowing.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    followingUID.add(ds.key!!)
                }

                val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Data").child("Video")
                val query: Query = ref.orderByChild("Time")
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        videoPostList.clear()
                        for(ds in snapshot.children) {
                            val modelPost : ModelPicturePost = ds.getValue(ModelPicturePost::class.java) as ModelPicturePost
                            if (followingUID.contains(modelPost.userID)) {
                                videoPostList.add(modelPost)
                                adapterVideoPost = AdapterVideoPosts(context!!, videoPostList)
                                recyclerViewVideo.adapter = adapterVideoPost
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


}