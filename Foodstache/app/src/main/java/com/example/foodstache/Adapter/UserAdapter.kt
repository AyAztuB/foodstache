package com.example.foodstache.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstache.Model.User
import com.example.foodstache.ProfileActivity
import com.example.foodstache.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class UserAdapter (private var mContext : Context,
                   private var mUser : List<User>,
                   private var isFragment : Boolean = false) : RecyclerView.Adapter<UserAdapter.ViewHolder>()
{
    private var firebaseUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.search_layout, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUser[position]
        holder.userNameTextView.text = user.getUsername()
        holder.fullNameTextView.text = user.getName()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.baseline_people_24).into(holder.profilPictureImageView)

        checkStatusFollow(user.getUid(), holder.followButton)

        holder.followButton.setOnClickListener {
            if (holder.followButton.text.toString()== "Follow"){
                firebaseUser?.uid.let { it->
                    FirebaseDatabase.getInstance().reference.child("Follow").child(it.toString())
                        .child("Following").child(user.getUid())
                        .setValue(true).addOnCompleteListener { task ->
                            if (task.isSuccessful){

                                firebaseUser?.uid.let { it->
                                    FirebaseDatabase.getInstance().reference.child("Follow").child(user.getUid())
                                        .child("Followers").child(it.toString())
                                        .setValue(true).addOnCompleteListener { task ->
                                            if (task.isSuccessful){

                                            }
                                        }
                                }
                            }
                        }
                }
            }
            else{
                firebaseUser?.uid.let { it->
                    FirebaseDatabase.getInstance().reference.child("Follow").child(it.toString())
                        .child("Following").child(user.getUid())
                        .removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                firebaseUser?.uid.let { it->
                                    FirebaseDatabase.getInstance().reference.child("Follow").child(user.getUid())
                                        .child("Followers").child(it.toString())
                                        .removeValue().addOnCompleteListener { task ->
                                            if (task.isSuccessful){

                                            }
                                        }
                                }
                            }
                        }
                }

            }
        }

        holder.profilPictureImageView.setOnClickListener {
            val intent = Intent(mContext, ProfileActivity::class.java)
            intent.putExtra("UID", user.getUid())
            mContext.startActivity(intent)
        }
    }


    class ViewHolder (@NonNull itemView : View) : RecyclerView.ViewHolder(itemView){
        var userNameTextView: TextView = itemView.findViewById(R.id.user_name_search)
        var fullNameTextView: TextView = itemView.findViewById(R.id.user_full_name_search)
        var profilPictureImageView: ImageView = itemView.findViewById(R.id.user_pp_search)
        var followButton : Button = itemView.findViewById(R.id.follow_btn)
    }

    private fun checkStatusFollow(uid: String, followButton: Button) {
        val followRef = firebaseUser?.uid.let { it ->
            FirebaseDatabase.getInstance().reference.child("Follow").child(it.toString())
                .child("Following")
        }
        followRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(uid).exists()){
                    followButton.text = "Following"
                }
                else{
                    followButton.text = "Follow"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}