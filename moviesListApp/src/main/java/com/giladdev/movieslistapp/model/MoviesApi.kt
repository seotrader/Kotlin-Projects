package com.giladdev.movieslistapp.model;

import io.reactivex.Single
import retrofit2.http.GET;

public interface MoviesApi {
    @GET("api/movies/")
    fun getMovies() : Single<MoviesObject>
}
    