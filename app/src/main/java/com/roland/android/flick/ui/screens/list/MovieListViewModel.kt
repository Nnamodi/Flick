package com.roland.android.flick.ui.screens.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.Category
import com.roland.android.domain.usecase.GetMovieListUseCase
import com.roland.android.flick.state.MovieListUiState
import com.roland.android.flick.state.State
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
	private val converter: ResponseConverter
) : ViewModel() {

	private val _movieListUiState = MutableStateFlow(MovieListUiState())
	var movieListUiState by mutableStateOf(_movieListUiState.value); private set
	private var lastCategoryFetched by mutableStateOf<Category?>(null)

	init {
		viewModelScope.launch {
			_movieListUiState.collect {
				movieListUiState = it
			}
		}
	}

	fun movieListActions(action: MovieListActions) {
		when (action) {
			is MovieListActions.LoadMovieList -> loadMovieList(action.category)
			is MovieListActions.Retry -> retry(action.categoryName)
		}
	}

	private fun loadMovieList(category: Category) {
		if (category == lastCategoryFetched) return
		_movieListUiState.value = MovieListUiState()
		viewModelScope.launch {
			movieListUseCase.execute(GetMovieListUseCase.Request(category))
				.map { converter.convertMovieListData(it) }
				.collect { data ->
					_movieListUiState.update { it.copy(movieData = data) }
					if (data is State.Success) lastCategoryFetched = category
				}
		}
	}

	private fun retry(categoryName: String) {
		val category = Category.valueOf(categoryName)
		_movieListUiState.value = MovieListUiState()
		loadMovieList(category)
	}

}