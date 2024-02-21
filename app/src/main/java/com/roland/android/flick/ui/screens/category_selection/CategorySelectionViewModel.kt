package com.roland.android.flick.ui.screens.category_selection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.Collection
import com.roland.android.domain.usecase.GetMoviesAndShowByGenreUseCase
import com.roland.android.flick.state.CategorySelectionUiState
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySelectionViewModel @Inject constructor(
	private val moviesAndShowByGenreUseCase: GetMoviesAndShowByGenreUseCase,
	private val converter: ResponseConverter
) : ViewModel() {

	private val _categorySelectionUiState = MutableStateFlow(CategorySelectionUiState())
	var categorySelectionUiState by mutableStateOf(_categorySelectionUiState.value); private set

	init {
		loadGenres()

		viewModelScope.launch {
			_categorySelectionUiState.collect {
				categorySelectionUiState = it
			}
		}
	}

	fun categorySelectionActions(action: CategorySelectionActions) {
		when (action) {
			is CategorySelectionActions.LoadMovieList -> loadMovieList(action.genreIdList, action.collection)
			CategorySelectionActions.Retry -> retry()
		}
	}

	private fun loadGenres() {
		viewModelScope.launch {
			moviesAndShowByGenreUseCase.execute(
				GetMoviesAndShowByGenreUseCase.Request(genreIds = "", collection = null)
			)
				.map { converter.convertCategorySelectionData(it) }
				.collect { data ->
					_categorySelectionUiState.update { it.copy(movieData = data) }
				}
		}
	}

	private fun loadMovieList(genreIdList: List<String>, collection: Collection) {
		_categorySelectionUiState.update {
			it.copy(
				movieData = null,
				selectedGenreIds = genreIdList,
				selectedCollection = collection
			)
		}
		viewModelScope.launch {
			val genreIds = genreIdList.joinToString(",")
			moviesAndShowByGenreUseCase.execute(
				GetMoviesAndShowByGenreUseCase.Request(genreIds, collection)
			)
				.map { converter.convertCategorySelectionData(it) }
				.collect { data ->
					_categorySelectionUiState.update { it.copy(movieData = data) }
				}
		}
	}

	private fun retry() {
		loadMovieList(
			genreIdList = categorySelectionUiState.selectedGenreIds,
			collection = categorySelectionUiState.selectedCollection
		)
	}

}