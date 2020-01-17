package com.giladdev.movieslistapp.model

import com.google.gson.annotations.SerializedName

class MoviesObject{
    @SerializedName("data")
    var moviesList : MutableList<Movie>?=null
}

data class Movie (
    @SerializedName("id")
    var movieId : String?,
    @SerializedName("title")
    var title : String?,
    @SerializedName("year")
    var year : String?,
    @SerializedName("genre")
    var genre : String?,
    @SerializedName("poster")
    var poster : String?
)