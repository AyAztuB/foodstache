package com.example.foodstache.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstache.Model.User
import com.example.foodstache.R


class UserAdapter (private var mContext : Context,
                   private var mUser : List<User>,
                   private var isFragment : Boolean = false) : RecyclerView.Adapter<UserAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class ViewHolder (@NonNull itemView : android.view.View) : RecyclerView.ViewHolder(itemView){
        var userNameTextView: TextView = itemView.findViewById(R.id.user_name_id_search)
    }


}