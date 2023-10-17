package com.example.architecturechallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.room.Room
import com.example.architecturechallenge.data.MoviesRepository
import com.example.architecturechallenge.data.local.LocalDataSource
import com.example.architecturechallenge.data.local.MoviesDatabase
import com.example.architecturechallenge.data.remote.RemoteDataSource
import com.example.architecturechallenge.ui.screen.home.Home

class MainActivity : ComponentActivity() {

    private lateinit var db: MoviesDatabase

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(applicationContext, MoviesDatabase::class.java, "movies-db")
            .fallbackToDestructiveMigration()
            .build()

        val repository = MoviesRepository(
            localDataSource = LocalDataSource(db.movieDao()),
            remote = RemoteDataSource(),
        )

        setContent {
            Home(repository)
        }
    }
}
