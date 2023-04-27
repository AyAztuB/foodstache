package com.example.foodstache.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstache.Model.ModelPicturePost
import com.example.foodstache.R
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterPicturePosts(val context: Context, val picturePostList: ArrayList<ModelPicturePost>):
    RecyclerView.Adapter<AdapterPicturePosts.PicturePostsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturePostsHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_picture_posts, parent, false)
        return PicturePostsHolder(view)
    }

    override fun onBindViewHolder(holder: PicturePostsHolder, position: Int) {
        val userID : String = picturePostList[position].userID
        val userPP : String = picturePostList[position].userPP
        val postID : String = picturePostList[position].postID
        val Time : String = picturePostList[position].Time
        val Image : String = picturePostList[position].Image
        val Username : String = picturePostList[position].Username
        val description : String = picturePostList[position].description

        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = Time.toLong() * -1
        val nTime : String = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString()

        holder.uppName.text = Username
        holder.ppTime.text = nTime
        holder.ppDescription.text = description

        try {
            Picasso.get().load(userPP).placeholder(R.drawable.baseline_people_24).into(holder.uppPP)
        } catch (_: Exception) {}

        try {
            Picasso.get().load(Image).into(holder.ppPicture)
        } catch (_: Exception) {}
    }

    override fun getItemCount(): Int {
        return picturePostList.size
    }

    // view holder class
    class PicturePostsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val uppPP : ImageView = itemView.findViewById(R.id.uppPP)
        val uppName : TextView = itemView.findViewById(R.id.uppName)
        val ppTime : TextView = itemView.findViewById(R.id.ppTime)
        val ppPicture : ImageView = itemView.findViewById(R.id.ppPicture)
        val ppDescription : TextView = itemView.findViewById(R.id.ppDescription)
    }
}