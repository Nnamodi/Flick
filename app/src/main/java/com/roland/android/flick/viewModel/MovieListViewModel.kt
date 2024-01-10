package com.roland.android.flick.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.Category
import com.roland.android.domain.usecase.GetMovieListUseCase
import com.roland.android.flick.state.MovieListUiState
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
	private val movieListUseCase: GetMovieListUseCase,
	private val converter: ResponseConverter,
) : ViewModel() {

	private val _movieListUiState = MutableStateFlow(MovieListUiState())
	var movieListUiState by mutableStateOf(_movieListUiState.value); private set

	init {
		viewModelScope.launch {
			_movieListUiState.collect {
				movieListUiState = it
			}
		}
	}

	fun prepareListScreen() {
		_movieListUiState.value = MovieListUiState()
	}

	fun loadMovieList(category: Category) {
		viewModelScope.launch {
			movieListUseCase.execute(GetMovieListUseCase.Request(category))
				.map { converter.convertMovieListData(it) }
				.collect { data ->
					_movieListUiState.update { it.copy(movieData = data) }
				}
		}
	}

}