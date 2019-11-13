package com.giladdev.recipefinderapp.UI

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.giladdev.recipefinderapp.R
import com.squareup.picasso.Picasso
import com.giladdev.recipefinderapp.model.Recipe

class RecipeListAdapter(private val list : ArrayList<Recipe>,
                        private val context : Context) : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    fun updateRecipes(newlist : ArrayList<Recipe>){
        list.clear()
        list.addAll(newlist)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.list_row,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    inner class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var title = itemView.findViewById<TextView>(R.id.recipeTitle)
        var thumbnail = itemView.findViewById<ImageView>(R.id.thumbnailImageView)
        var ingridentes =  itemView.findViewById<TextView>(R.id.recipeIngrediant)
        var linkButton = itemView.findViewById<Button>(R.id.linkButton)

        fun bindView(recipe : Recipe){
            title.text = recipe.title
            ingridentes.text = "Ingredients: "+recipe.ingredients

            if (!TextUtils.isEmpty(recipe.thumbnail)){
                Picasso.get().load(recipe.thumbnail)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(thumbnail)
            }else
            {
                Picasso.get().load(R.mipmap.ic_launcher)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(thumbnail)
            }

            linkButton.setOnClickListener {
                var intent = Intent(context,ShowLinkActivity::class.java)

                intent.putExtra("link", recipe.link.toString())
                context.startActivity(intent)
            }
        }

    }
}