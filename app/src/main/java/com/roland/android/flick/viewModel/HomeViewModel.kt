package com.roland.android.flick.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetFurtherMovieCollectionUseCase
import com.roland.android.domain.usecase.GetMoviesUseCase
import com.roland.android.flick.models.FurtherMoviesModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.state.State
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val moviesUseCase: GetMoviesUseCase,
	private val furtherMovieUseCase: GetFurtherMovieCollectionUseCase,
	private val converter: ResponseConverter,
) : ViewModel() {

	private val _moviesFlow = MutableStateFlow<State<MoviesModel>?>(null)
	var moviesFlow by mutableStateOf(_moviesFlow.value); private set

	private val _furtherMoviesFlow = MutableStateFlow<State<FurtherMoviesModel>?>(null)
	var furtherMoviesFlow by mutableStateOf(_furtherMoviesFlow.value); private set

	init {
		loadMovies()
		loadFurtherMovieCollections()

		viewModelScope.launch {
			_moviesFlow.collect {
				moviesFlow = it
				Log.i("MoviesInfo", "Fetched movies: $moviesFlow")
			}
		}
		viewModelScope.launch {
			_furtherMoviesFlow.collect {
				furtherMoviesFlow = it
				Log.i("MoviesInfo", "Fetched more movie collections: $furtherMoviesFlow")
			}
		}
	}

	private fun loadMovies() {
		viewModelScope.launch {
			moviesUseCase.execute(GetMoviesUseCase.Request)
				.map { converter.convertMoviesData(it) }
				.collect { data ->
//					moviesState.update { it.copy(movies = data) }
					_moviesFlow.value = data
				}
		}
	}

	private fun loadFurtherMovieCollections() {
		viewModelScope.launch {
			furtherMovieUseCase.execute(GetFurtherMovieCollectionUseCase.Request)
				.map { converter.convertFurtherMoviesData(it) }
				.collect { data ->
//					moviesState.update { it.copy(furtherMovies = data) }
					_furtherMoviesFlow.value = data
				}
		}
	}

}