package com.example.foodstache.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstache.Model.ModelRecipePost
import com.example.foodstache.R
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class AdapterRecipePosts(val context: Context, val recipePostList: ArrayList<ModelRecipePost>):
    RecyclerView.Adapter<AdapterRecipePosts.RecipePostsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRecipePosts.RecipePostsHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_recipe_posts, parent, false)
        return AdapterRecipePosts.RecipePostsHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterRecipePosts.RecipePostsHolder, position: Int) {
        val userID : String = recipePostList[position].userID
        val userPP : String = recipePostList[position].userPP
        val Time : String = recipePostList[position].Time
        val Username : String = recipePostList[position].Username
        val title : String = recipePostList[position].title
        val Ingredients : ArrayList<String> = recipePostList[position].Ingredients
        val Steps : ArrayList<String> = recipePostList[position].Steps

        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = Time.toLong() * -1
        val nTime : String = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString()

        holder.urpName.text = Username
        holder.rpTime.text = nTime
        holder.rpTitle.text = title

        var ingredients : String = ""
        var steps : String = ""

        for(i in Ingredients.indices) {
            if (i != 0)
                ingredients += "\n"
            ingredients += "Ingredient ${i+1}: ${Ingredients[i]}"
        }

        for(i in Steps.indices) {
            if (i != 0)
                steps += "\n"
            steps += "Step ${i+1}: ${Steps[i]}"
        }

        holder.rpIngredients.text = ingredients
        holder.rpSteps.text = steps

        try {
            Picasso.get().load(userPP).placeholder(R.drawable.baseline_people_24).into(holder.urpPP)
        } catch (_: Exception) {}
    }

    override fun getItemCount(): Int {
        return recipePostList.size
    }

    class RecipePostsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val urpPP : ImageView = itemView.findViewById(R.id.urpPP)
        val urpName : TextView = itemView.findViewById(R.id.urpName)
        val rpTime : TextView = itemView.findViewById(R.id.rpTime)
        val rpTitle : TextView = itemView.findViewById(R.id.rpTitle)
        val rpIngredients : TextView = itemView.findViewById(R.id.rpIngredients)
        val rpSteps : TextView = itemView.findViewById(R.id.rpSteps)
    }
}