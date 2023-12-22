package com.roland.android.flick.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetMoviesUseCase
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
	private val converter: ResponseConverter
) : ViewModel() {

	private val moviesFlow = MutableStateFlow<UiState<MoviesModel>?>(null)
	var movies by mutableStateOf(moviesFlow.value ?: UiState.Loading); private set

	init {
		loadMovies()

		viewModelScope.launch {
			moviesFlow.collect {
				movies = it ?: UiState.Loading
				Log.i("MoviesInfo", "Fetched: $it")
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

}