package com.example.architecturechallenge.data.remote

import com.example.architecturechallenge.data.Movie
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource() {
    suspend fun getMovies(): List<Movie> {
        return Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(MoviesServices::class.java)
            .getMovies()
            .results
            .map { it.toMovie() }
    }
}
