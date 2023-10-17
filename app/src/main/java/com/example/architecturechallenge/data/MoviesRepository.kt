package com.example.architecturechallenge.data

import com.example.architecturechallenge.data.local.LocalDataSource
import com.example.architecturechallenge.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class MoviesRepository(
    private val localDataSource: LocalDataSource,
    private val remote: RemoteDataSource,
) {

    val movies: Flow<List<Movie>> = localDataSource.movies

    suspend fun requestMovies() {
        val isDbEmpty = localDataSource.countMovies() == 0
        if (isDbEmpty) {
            localDataSource.insertAll(remote.getMovies())
        }
    }

    suspend fun updateMovie(movie: Movie) {
        localDataSource.updateMovie(movie)
    }
}
// calle 55 #21-30
