package com.example.architecturechallenge.data.remote

import retrofit2.http.GET

interface MoviesServices {

    @GET("discover/movie?api_key=56ac9438cdd9261ba7472319de1c023c")
    suspend fun getMovies(): MovieResult
}
