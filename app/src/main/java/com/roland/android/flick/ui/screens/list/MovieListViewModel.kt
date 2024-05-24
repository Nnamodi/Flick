package com.roland.android.flick.ui.screens.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.Category
import com.roland.android.domain.usecase.Category.FAVORITED_MOVIES
import com.roland.android.domain.usecase.Category.FAVORITED_SERIES
import com.roland.android.domain.usecase.Category.RATED_MOVIES
import com.roland.android.domain.usecase.Category.RATED_SERIES
import com.roland.android.domain.usecase.Category.WATCHLISTED_MOVIES
import com.roland.android.domain.usecase.Category.WATCHLISTED_SERIES
import com.roland.android.domain.usecase.GetMovieListUseCase
import com.roland.android.flick.models.accountSessionId
import com.roland.android.flick.models.updatedMediaCategory
import com.roland.android.flick.models.userAccountDetails
import com.roland.android.flick.models.userAccountId
import com.roland.android.flick.state.MovieListUiState
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
class MovieListViewModel @Inject constructor(
	private val movieListUseCase: GetMovieListUseCase,
	private val networkConnectivity: NetworkConnectivity,
	private val converter: ResponseConverter
) : ViewModel() {

	private val _movieListUiState = MutableStateFlow(MovieListUiState())
	var movieListUiState by mutableStateOf(_movieListUiState.value); private set
	private var lastCategoryFetched by mutableStateOf<Category?>(null)
	private var accountId by mutableStateOf("")
	private var userId by mutableIntStateOf(0)
	private var sessionId by mutableStateOf("")
	private var shouldAutoReloadData by mutableStateOf(true)

	init {
		viewModelScope.launch {
			userAccountId.collect { id ->
				accountId = id
			}
		}
		viewModelScope.launch {
			userAccountDetails.collect { user ->
				userId = user?.id ?: 0
			}
		}
		viewModelScope.launch {
			accountSessionId.collect { id ->
				sessionId = id ?: ""
			}
		}
		viewModelScope.launch {
			updatedMediaCategory.collect {
				val accountMediaCategories = setOf(WATCHLISTED_MOVIES, WATCHLISTED_SERIES, FAVORITED_MOVIES, FAVORITED_SERIES, RATED_MOVIES, RATED_SERIES)
				if (lastCategoryFetched in accountMediaCategories) _movieListUiState.value = MovieListUiState()
			}
		}
		viewModelScope.launch {
			_movieListUiState.collect {
				movieListUiState = it
			}
		}
		viewModelScope.launch {
			autoReloadData.collect {
				shouldAutoReloadData = it
			}
		}
		viewModelScope.launch {
			networkConnectivity.observe().collect { status ->
				if (!shouldAutoReloadData ||
					(status == NetworkConnectivity.Status.Offline) ||
					(movieListUiState.movieData !is State.Error)) return@collect
				lastCategoryFetched?.name?.let { retry(it) }
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
		if (category == lastCategoryFetched && movieListUiState.movieData !is State.Error) return
		lastCategoryFetched = category
		_movieListUiState.value = MovieListUiState()
		viewModelScope.launch {
			movieListUseCase.execute(
				GetMovieListUseCase.Request(
					category = category,
					accountId = accountId,
					userId = userId,
					sessionId = sessionId
				)
			)
				.map { converter.convertMovieListData(it) }
				.collect { data ->
					_movieListUiState.update { it.copy(movieData = data) }
				}
		}
	}

	private fun retry(categoryName: String) {
		val category = Category.valueOf(categoryName)
		_movieListUiState.value = MovieListUiState()
		loadMovieList(category)
	}

}