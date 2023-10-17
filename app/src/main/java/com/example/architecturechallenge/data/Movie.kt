package com.example.architecturechallenge.data

import androidx.room.Entity

@Entity
data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val overview: String,
    val favorite: Boolean,
)
