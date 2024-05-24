package com.roland.android.flick.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetSearchedMoviesUseCase
import com.roland.android.flick.state.SearchUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.state.autoReloadData
import com.roland.android.flick.utils.ResponseConverter
import com.roland.android.flick.utils.network.NetworkConnectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val searchedMoviesUseCase: GetSearchedMoviesUseCase,
	private val networkConnectivity: NetworkConnectivity,
	private val converter: ResponseConverter
) : ViewModel() {

	private val _searchUiState = MutableStateFlow(SearchUiState())
	var searchUiState by mutableStateOf(_searchUiState.value); private set
	private var shouldAutoReloadData by mutableStateOf(true)

	init {
		viewModelScope.launch {
			_searchUiState.collect {
				searchUiState = it
			}
		}
		viewModelScope.launch {
			autoReloadData.collect {
				shouldAutoReloadData = it
			}
		}
		viewModelScope.launch {
			networkConnectivity.observe().collect {
				if (!shouldAutoReloadData ||
					(it == NetworkConnectivity.Status.Offline) ||
					(searchUiState.movieData !is State.Error)) return@collect
				retry(searchUiState.searchQuery)
			}
		}
	}

	fun searchActions(action: SearchActions) {
		when (action) {
			is SearchActions.Retry -> retry(action.query)
			is SearchActions.Search -> search(action.query, action.category)
			is SearchActions.ToggleCategory -> toggleCategory(action.searchCategory)
		}
	}

	private fun search(query: String, searchCategory: SearchCategory) {
		_searchUiState.update {
			it.copy(
				movieData = null,
				searchCategory = searchCategory,
				searchQuery = query
			)
		}
		viewModelScope.launch {
			searchedMoviesUseCase.execute(GetSearchedMoviesUseCase.Request(query))
				.map { converter.convertSearchedMovieData(it) }
				.collect { data ->
					_searchUiState.update { it.copy(movieData = data) }
				}
		}
	}

	private fun retry(query: String) {
		search(query, searchUiState.searchCategory)
	}

	private fun toggleCategory(searchCategory: SearchCategory) {
		_searchUiState.update {
			it.copy(searchCategory = searchCategory)
		}
	}

}