package com.giladdev.movieslistapp.View

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.giladdev.movieslistapp.R
import com.giladdev.movieslistapp.model.Movie
import com.squareup.picasso.Picasso

class MoviesAdapter(private var movieList:MutableList<Movie>) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>(),
    Filterable {

    // saved list that will not be the filtered one
    var savedFullMovieList = mutableListOf<Movie>()


    var moviesFilter = object :Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredMoviesList = mutableListOf<Movie>()
            movieList.clear()
            movieList.addAll(savedFullMovieList)

            if (constraint == null || constraint.length==0)
                filteredMoviesList.addAll(savedFullMovieList)
            else{
                var filterPattern = constraint.toString().toLowerCase().trim()

                var filteredList = movieList.filter {

                    (it.title!!.toLowerCase().contains(filterPattern) == true) ||
                            (it.genre!!.toLowerCase().contains(filterPattern) == true)

                }

                filteredMoviesList.addAll(filteredList)
            }

            var results = FilterResults()

            results.values = filteredMoviesList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            movieList.clear()

            @Suppress("UNCHECKED_CAST")
            val result: MutableList<Movie> = results!!.values as MutableList<Movie>

            movieList.addAll(result)

            if ( constraint==null ||  constraint.length==0)
            {
                if (movieList.isEmpty()){
                    movieList.addAll(savedFullMovieList)
                }

            }
            notifyDataSetChanged()
        }
    }


    override fun getFilter(): Filter {
        return moviesFilter
    }

    fun updateMoviers(movies : MutableList<Movie>)
    {

        movieList.clear()
        movieList.addAll(movies)
        savedFullMovieList.addAll(movies)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(movie : Movie){
            val title = itemView.findViewById<TextView>(R.id.TVMovieTitle)
            val year = itemView.findViewById<TextView>(R.id.TVMovieYear)
            val genre = itemView.findViewById<TextView>(R.id.TVGenre)
            val image = itemView.findViewById<ImageView>(R.id.IVMovie)

            title.text = movie.title
            year.text = movie.year
            genre.text = movie.genre+"-"

            Picasso.get().load(movie.poster)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create our view from the XML file
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_list, parent, false)


        return ViewHolder(view)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bindItem(movieList[position])
    }
}