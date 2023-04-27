package com.example.foodstache.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstache.Model.ModelPicturePost
import com.example.foodstache.R
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class AdapterVideoPosts(val context: Context, val videoPostList: ArrayList<ModelPicturePost>):
    RecyclerView.Adapter<AdapterVideoPosts.VideoPostsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPostsHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_video_posts, parent, false)
        return AdapterVideoPosts.VideoPostsHolder(view)
    }

    override fun onBindViewHolder(holder: VideoPostsHolder, position: Int) {
        val userID : String = videoPostList[position].userID
        val userPP : String = videoPostList[position].userPP
        val postID : String = videoPostList[position].postID
        val Time : String = videoPostList[position].Time
        val Image : String = videoPostList[position].Image
        val Username : String = videoPostList[position].Username
        val description : String = videoPostList[position].description

        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = Time.toLong() * -1
        val nTime : String = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString()

        holder.uvpName.text = Username
        holder.vpTime.text = nTime
        holder.vpDescription.text = description

        try {
            Picasso.get().load(userPP).placeholder(R.drawable.baseline_people_24).into(holder.uvpPP)
        } catch (_: Exception) {}

        try {
            val videoUri = Uri.parse(Image)
            holder.vpVideo.setVideoURI(videoUri)

            val mediaController = MediaController(context)
            mediaController.setAnchorView(holder.vpVideo)
            holder.vpVideo.setMediaController(mediaController)

            holder.vpVideo.start()
        } catch (_: Exception) {}
    }

    override fun getItemCount(): Int {
        return videoPostList.size
    }

    class VideoPostsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val uvpPP : ImageView = itemView.findViewById(R.id.uvpPP)
        val uvpName : TextView = itemView.findViewById(R.id.uvpName)
        val vpTime : TextView = itemView.findViewById(R.id.vpTime)
        val vpVideo : VideoView = itemView.findViewById(R.id.vpVideo)
        val vpDescription : TextView = itemView.findViewById(R.id.vpDescription)
    }
}