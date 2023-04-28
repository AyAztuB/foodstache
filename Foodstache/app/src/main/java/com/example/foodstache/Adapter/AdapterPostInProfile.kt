package com.example.foodstache.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstache.Model.ModelPostInProfile
import com.example.foodstache.ProfileActivity
import com.example.foodstache.R
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class AdapterPostInProfile(val context: Context, val postList: ArrayList<ModelPostInProfile>):
    RecyclerView.Adapter<AdapterPostInProfile.PostInProfileHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPostInProfile.PostInProfileHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_posts_in_profile, parent, false)
        return AdapterPostInProfile.PostInProfileHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterPostInProfile.PostInProfileHolder, position: Int) {
        val userID : String = postList[position].userID
        val userPP : String = postList[position].userPP
        val Time : String = postList[position].Time
        val Username : String = postList[position].Username

        val picture : String = postList[position].picture
        val video : String = postList[position].video
        val description : String = postList[position].description
        val title : String = postList[position].title
        val Ingredients : ArrayList<String>? = postList[position].Ingredients
        val Steps : ArrayList<String>? = postList[position].Steps

        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = Time.toLong() * -1
        val nTime : String = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString()

        holder.upName.text = Username
        holder.pTime.text = nTime
        try {
            Picasso.get().load(userPP).placeholder(R.drawable.baseline_people_24).into(holder.upPP)
        } catch (_: Exception) {}

        if (picture.equals("noImage")) {

            if (video.equals("noVideo")) {

                holder.pTitle.text = title

                var ingredients : String = ""
                var steps : String = ""

                if (Ingredients == null || Steps == null) {
                    Toast.makeText(context, "no steps / no ingredients", Toast.LENGTH_SHORT).show()
                } else {
                    for(i in Ingredients.indices) {
                        if (i != 0)
                            ingredients += "\n"
                        ingredients += "Ingredient ${i+1}: ${Ingredients[i]}"
                    }

                    for(i in Steps.indices) {
                        if (i != 0)
                            steps += "\n"
                        steps += "${i+1}. ${Steps[i]}"
                    }
                }

                holder.pIngredients.text = ingredients
                holder.pSteps.text = steps

                holder.pPicture.visibility = View.GONE
                holder.pVideo.visibility = View.GONE
                holder.pDescription.visibility = View.GONE
            } else {

                holder.pDescription.text = description

                try {
                    val videoUri = Uri.parse(video)
                    holder.pVideo.setVideoURI(videoUri)

                    val mediaController = MediaController(context)
                    mediaController.setAnchorView(holder.pVideo)
                    holder.pVideo.setMediaController(mediaController)

                    holder.pVideo.start()
                } catch (_: Exception) {}

                holder.pPicture.visibility = View.GONE
                holder.recipeLayer.visibility = View.GONE
            }
        } else {

            holder.pDescription.text = description

            try {
                Picasso.get().load(picture).into(holder.pPicture)
            } catch (_: Exception) {}

            holder.pVideo.visibility = View.GONE
            holder.recipeLayer.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class PostInProfileHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val upPP : ImageView = itemView.findViewById(R.id.upPP)
        val upName : TextView = itemView.findViewById(R.id.upName)
        val pTime : TextView = itemView.findViewById(R.id.pTime)
        val pPicture : ImageView = itemView.findViewById(R.id.pPicture)
        val pVideo : VideoView = itemView.findViewById(R.id.pVideo)
        val pDescription : TextView = itemView.findViewById(R.id.pDescription)
        val recipeLayer : LinearLayout = itemView.findViewById(R.id.recipe_post_part)
        val pTitle : TextView = itemView.findViewById(R.id.pTitle)
        val pIngredients : TextView = itemView.findViewById(R.id.pIngredients)
        val pSteps : TextView = itemView.findViewById(R.id.pSteps)
    }
}