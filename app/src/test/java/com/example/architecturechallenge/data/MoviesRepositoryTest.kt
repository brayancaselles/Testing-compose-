package com.example.architecturechallenge.data

import com.example.architecturechallenge.data.local.LocalDataSource
import com.example.architecturechallenge.data.remote.RemoteDataSource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

class MoviesRepositoryTest {

    @Test
    fun `When DB is empty, server is called`() {
        val localDataSource = mock<LocalDataSource>() { onBlocking { countMovies() } doReturn 0 }
        val remoteDataSource = mock<RemoteDataSource>()
        val repository = MoviesRepository(localDataSource, remoteDataSource)

        runBlocking { repository.requestMovies() }

        verifyBlocking(remoteDataSource) { getMovies() }
    }

    @Test
    fun `When DB is empty, movies are saved into DB`() {
        val expectedMovies = listOf(
            Movie(1, "Movie 1", "overview", "poster", false),
            Movie(2, "Movie 2", "overview", "poster", false),
        )
        val localDataSource = mock<LocalDataSource>() { onBlocking { countMovies() } doReturn 0 }
        val remoteDataSource =
            mock<RemoteDataSource>() { onBlocking { getMovies() } doReturn expectedMovies }
        val repository = MoviesRepository(localDataSource, remoteDataSource)

        runBlocking { repository.requestMovies() }

        verifyBlocking(localDataSource) { insertAll(expectedMovies) }
    }

    @Test
    fun `When DB is not empty, remote data source is not called`() {
        val localDataSource = mock<LocalDataSource>() { onBlocking { countMovies() } doReturn 1 }
        val remoteDataSource = mock<RemoteDataSource>()
        val repository = MoviesRepository(localDataSource, remoteDataSource)

        runBlocking { repository.requestMovies() }

        verifyBlocking(remoteDataSource, times(0)) { getMovies() }
    }

    @Test
    fun `When DB is not empty, movies are recovered from DB`() {
        val localMovies = listOf(
            Movie(1, "Movie 1", "overview 1", "poster 1", false),
            Movie(2, "Movie 2", "overview 2", "poster 2", false),
        )
        val remoteMovies = listOf(
            Movie(3, "Movie 3", "overview 3", "poster 3", false),
            Movie(4, "Movie 4", "overview 4", "poster 4", false),
        )
        val localDataSource = mock<LocalDataSource>() {
            onBlocking { countMovies() } doReturn 1
            onBlocking { movies } doReturn flowOf(localMovies)
        }
        val remoteDataSource = mock<RemoteDataSource>()
        val repository = MoviesRepository(localDataSource, remoteDataSource)

        val movies = runBlocking {
            repository.requestMovies()
            repository.movies.first()
        }

        assertEquals(localMovies, movies)
    }
}
