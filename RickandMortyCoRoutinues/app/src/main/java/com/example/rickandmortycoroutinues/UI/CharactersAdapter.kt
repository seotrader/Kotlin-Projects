package com.example.rickandmortycoroutinues.UI

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortycoroutinues.Data.Character
import com.example.rickandmortycoroutinues.R

class CharactersAdapter(private val list: ArrayList<Character>,private val context: Context): RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {

    init {
        list.clear()
    }

    fun updateRV(character: ArrayList<Character>) {
        list.clear()
        list.addAll(character)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.character_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CharactersAdapter.ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(character: Character) {
            var name: TextView = itemView.findViewById<TextView>(R.id.nameTV)
            var gender: TextView = itemView.findViewById<TextView>(R.id.genderTV)

            name.text = character.name
            gender.text = character.gender
        }
    }
}