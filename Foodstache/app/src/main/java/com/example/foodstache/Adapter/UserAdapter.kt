package com.example.foodstache.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstache.Model.User
import com.example.foodstache.R
import com.squareup.picasso.Picasso


class UserAdapter (private var mContext : Context,
                   private var mUser : List<User>,
                   private var isFragment : Boolean = false) : RecyclerView.Adapter<UserAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.search_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUser[position]
        holder.userNameTextView.text = user.getUsername()
        holder.fullNameTextView.text = user.getFullName()
        Picasso.get().load(user.getPp()).placeholder(R.drawable.profile_image).into(holder.profilPictureImageView)
    }

    class ViewHolder (@NonNull itemView : View) : RecyclerView.ViewHolder(itemView){
        var userNameTextView: TextView = itemView.findViewById(R.id.user_name_id_search)
        var fullNameTextView: TextView = itemView.findViewById(R.id.full_name)
        var profilPictureImageView: ImageView = itemView.findViewById(R.id.profile_image)
        var bioTextView: TextView = itemView.findViewById(R.id.bio)
        var totalPostsTextView: TextView = itemView.findViewById(R.id.total_posts)
        var totalFollowersTextView: TextView = itemView.findViewById(R.id.total_followers)
        var totalFollowingTextView: TextView = itemView.findViewById(R.id.total_following)
        var followButton : Button = itemView.findViewById(R.id.followButton)
    }


}