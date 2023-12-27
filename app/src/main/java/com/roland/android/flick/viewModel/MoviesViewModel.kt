package com.roland.android.flick.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetFurtherMovieCollectionUseCase
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetMoviesUseCase
import com.roland.android.flick.models.FurtherMoviesModel
import com.roland.android.flick.models.MovieDetailsModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.state.UiState
import com.roland.android.flick.utils.NavigationActions
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
	private val moviesUseCase: GetMoviesUseCase,
	private val furtherMovieUseCase: GetFurtherMovieCollectionUseCase,
	private val movieDetailsUseCase: GetMovieDetailsUseCase,
	private val converter: ResponseConverter
) : ViewModel() {

	private val moviesFlow = MutableStateFlow<UiState<MoviesModel>?>(null)
	var movies by mutableStateOf(moviesFlow.value ?: UiState.Loading); private set

	private val furtherMoviesFlow = MutableStateFlow<UiState<FurtherMoviesModel>?>(null)
	var furtherMovies by mutableStateOf(furtherMoviesFlow.value ?: UiState.Loading); private set

	private val movieDetailsFlow = MutableStateFlow<UiState<MovieDetailsModel>?>(null)
	var movieDetails by mutableStateOf(movieDetailsFlow.value ?: UiState.Loading); private set

	init {
		loadMovies()
		loadFurtherMovieCollections()

		viewModelScope.launch {
			moviesFlow.collect {
				movies = it ?: UiState.Loading
				Log.i("MoviesInfo", "Fetched: $movies")
			}
		}
		viewModelScope.launch {
			furtherMoviesFlow.collect {
				furtherMovies = it ?: UiState.Loading
				Log.i("MoviesInfo", "Fetched more collections: $furtherMovies")
			}
		}
		viewModelScope.launch {
			movieDetailsFlow.collect {
				movieDetails = it ?: UiState.Loading
				Log.i("MoviesInfo", "Fetched movie details: $movieDetails")
			}
		}
	}

	private fun loadMovies() {
		viewModelScope.launch {
			moviesUseCase.execute(GetMoviesUseCase.Request)
				.map { converter.convertMoviesData(it) }
				.collect {
					moviesFlow.value = it
				}
		}
	}

	private fun loadFurtherMovieCollections() {
		viewModelScope.launch {
			furtherMovieUseCase.execute(GetFurtherMovieCollectionUseCase.Request)
				.map { converter.convertFurtherMoviesData(it) }
				.collect {
					furtherMoviesFlow.value = it
				}
		}
	}

	fun navigationActions(action: NavigationActions) {
		when (action) {
			is NavigationActions.GetMovieDetails -> getMovieDetails(action.movieId)
		}
	}

	private fun getMovieDetails(movieId: Int) {
		viewModelScope.launch {
			movieDetailsUseCase.execute(GetMovieDetailsUseCase.Request(movieId))
				.map { converter.convertMovieDetailsData(it) }
				.collect {
					movieDetailsFlow.value = it
				}
		}
	}

}