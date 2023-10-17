package com.example.architecturechallenge.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architecturechallenge.data.Movie
import com.example.architecturechallenge.data.MoviesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MoviesRepository) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            delay(2000)
            repository.requestMovies()
            repository.movies.collect { movies ->
                _state.value = UiState(
                    loading = false,
                    movies = movies,
                )
            }
        }
    }

    fun onMovieClick(movie: Movie) {
        viewModelScope.launch {
            repository.updateMovie(movie.copy(favorite = !movie.favorite))
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList(),
    )
}
