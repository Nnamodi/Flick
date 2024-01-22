package com.roland.android.flick.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetSearchedMoviesUseCase
import com.roland.android.domain.usecase.SearchCategory
import com.roland.android.flick.state.SearchUiState
import com.roland.android.flick.utils.ResponseConverter
import com.roland.android.flick.utils.SearchActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val searchedMoviesUseCase: GetSearchedMoviesUseCase,
	private val converter: ResponseConverter
) : ViewModel() {

	private val _searchUiState = MutableStateFlow(SearchUiState())
	var searchUiState by mutableStateOf(_searchUiState.value); private set

	init {
		viewModelScope.launch {
			_searchUiState.collect {
				searchUiState = it
			}
		}
	}

	fun searchActions(action: SearchActions) {
		when (action) {
			is SearchActions.Search -> search(action.query, action.category)
			is SearchActions.Retry -> retry(action.query)
		}
	}

	private fun search(query: String, searchCategory: SearchCategory) {
		viewModelScope.launch {
			searchedMoviesUseCase.execute(GetSearchedMoviesUseCase.Request(query, searchCategory))
				.map { converter.convertSearchedMovieData(it) }
				.collect { data ->
					_searchUiState.update {
						it.copy(
							movieData = data,
							searchCategory = searchCategory,
							searchQuery = query
						)
					}
				}
		}
	}

	private fun retry(query: String) {
		search(query, searchUiState.searchCategory)
	}

}