package com.roland.android.flick.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetFurtherMovieCollectionUseCase
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetMoviesUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import com.roland.android.flick.models.FurtherMoviesModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.state.UiState
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
	private val converter: ResponseConverter,
	movieDetailsUseCase: GetMovieDetailsUseCase,
	tvShowDetailsUseCase: GetTvShowDetailsUseCase
) : BaseViewModel(movieDetailsUseCase, tvShowDetailsUseCase, converter) {

	private val moviesFlow = MutableStateFlow<UiState<MoviesModel>?>(null)
	var movies by mutableStateOf(moviesFlow.value ?: UiState.Loading); private set

	private val furtherMoviesFlow = MutableStateFlow<UiState<FurtherMoviesModel>?>(null)
	var furtherMovies by mutableStateOf(furtherMoviesFlow.value ?: UiState.Loading); private set

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

}