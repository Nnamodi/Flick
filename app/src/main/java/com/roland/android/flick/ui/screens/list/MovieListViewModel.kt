package com.roland.android.flick.ui.screens.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.entity.auth_response.Response
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
import com.roland.android.flick.utils.MediaUtil
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
	private val mediaUtil: MediaUtil,
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
	private val accountMediaCategories = setOf(WATCHLISTED_MOVIES, WATCHLISTED_SERIES, FAVORITED_MOVIES, FAVORITED_SERIES, RATED_MOVIES, RATED_SERIES)

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
			updatedMediaCategory.collect { category ->
				if (category == null) return@collect
				if (lastCategoryFetched in accountMediaCategories) {
					lastCategoryFetched?.let { loadMovieList(category = it, refresh = true) }
				}
				updatedMediaCategory.value = null
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
				lastCategoryFetched?.let { retry(it.name) }
			}
		}
	}

	fun movieListActions(action: MovieListActions?) {
		when (action) {
			is MovieListActions.LoadMovieList -> loadMovieList(action.category)
			is MovieListActions.Retry -> retry(action.categoryName)
			is MovieListActions.RemoveFromList -> removeFromList(action.mediaId, action.mediaType)
			null -> _movieListUiState.update { it.copy(response = null) }
		}
	}

	private fun loadMovieList(category: Category, refresh: Boolean = false) {
		if ((category == lastCategoryFetched) && !refresh &&
			(movieListUiState.movieData !is State.Error)) return
		lastCategoryFetched = category
		_movieListUiState.value = MovieListUiState(isCancellable = category in accountMediaCategories)
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

	private fun removeFromList(mediaId: Int, mediaType: String) {
		val updateResponse: (State<Response>) -> Unit = { response ->
			_movieListUiState.update { it.copy(response = response) }
		}
		when (lastCategoryFetched) {
			in setOf(WATCHLISTED_MOVIES, WATCHLISTED_SERIES) -> {
				mediaUtil.watchlistMedia(
					accountId = userId,
					sessionId = sessionId,
					mediaId = mediaId,
					mediaType = mediaType,
					watchlist = false,
					result = { updateResponse(it) }
				)
			}
			in setOf(FAVORITED_MOVIES, FAVORITED_SERIES) -> {
				mediaUtil.favoriteMedia(
					accountId = userId,
					sessionId = sessionId,
					mediaId = mediaId,
					mediaType = mediaType,
					favorite = false,
					result = { updateResponse(it) }
				)
			}
			in setOf(RATED_MOVIES, RATED_SERIES) -> {
				mediaUtil.deleteMediaRating(
					sessionId = sessionId,
					mediaId = mediaId,
					mediaType = mediaType,
					result = { updateResponse(it) }
				)
			}
			else -> {}
		}
	}

	private fun retry(categoryName: String) {
		val category = Category.valueOf(categoryName)
		_movieListUiState.value = MovieListUiState()
		loadMovieList(category)
	}

}