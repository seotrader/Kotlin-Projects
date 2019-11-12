package com.giladdev.countires.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.giladdev.countires.Model.Country
import com.giladdev.countires.R
import com.giladdev.countires.Util.getProgressDrawable
import com.giladdev.countires.Util.loadImage
import kotlinx.android.synthetic.main.country_line.view.*

class CountryListAdaptor( var countries: ArrayList<Country>) :RecyclerView.Adapter<CountryListAdaptor.CountryViewHolder>(){
    fun updateCountries(newCountries: List<Country>)
    {
        countries.clear()
        countries.addAll(newCountries)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.country_line,parent,false)
    )


    override fun getItemCount(): Int {
       return countries.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val countryName = view.countryTextVIew
        private val countryCapital = view.capitalTextView
        private val imageView = view.imageView
        private val progressDrawable = getProgressDrawable(view.context)

        fun bind(country : Country){
            countryName.text = country.countryName
            countryCapital.text = country.capital
            imageView.loadImage(country.flag, progressDrawable)
    }


}
}