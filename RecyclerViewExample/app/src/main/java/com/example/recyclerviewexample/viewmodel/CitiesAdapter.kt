package com.example.recyclerviewexample.viewmodel

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewexample.R
import com.example.recyclerviewexample.model.City
import org.w3c.dom.Text

class CitiesAdapter(private val cities: List<City>) : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cities_row, parent, false))
    }

    override fun getItemCount() = cities.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cityText = holder.itemView.findViewById<TextView>(R.id.city_text_view)

        if (cities[position].city.toString().isEmpty()) {
            cityText.text = cities[position].country.toString()
            cityText.typeface = Typeface.DEFAULT_BOLD
        } else {
            cityText.text = cities[position].city.toString()
            cityText.typeface = Typeface.DEFAULT
        }
    }
}