package com.roland.android.flick.ui.screens.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.AccountUseCase
import com.roland.android.domain.usecase.MediaActions
import com.roland.android.domain.usecase.MediaType
import com.roland.android.domain.usecase.MediaUtilUseCase
import com.roland.android.flick.models.userAccountDetails
import com.roland.android.flick.models.userAccountId
import com.roland.android.flick.state.AccountUiState
import com.roland.android.flick.utils.MediaUtil
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
	private val accountUseCase: AccountUseCase,
	private val mediaUtilUseCase: MediaUtilUseCase,
	private val converter: ResponseConverter,
	private val mediaUtil: MediaUtil
) : ViewModel() {

	private val _accountUiState = MutableStateFlow(AccountUiState())
	var accountUiState by mutableStateOf(_accountUiState.value); private set
	private var accountId by mutableStateOf("")
	private var userId by mutableIntStateOf(0)
	var userLoggedIn by mutableStateOf(false); private set

	init {
		viewModelScope.launch {
			userAccountDetails.collect { data ->
				if (data == null) return@collect
				_accountUiState.update { it.copy(accountDetails = data) }
			}
		}
		viewModelScope.launch {
			userAccountId.collect {
				accountId = it
				userLoggedIn = it.isNotEmpty()
				if (!userLoggedIn) return@collect
				fetchMovieData(); fetchShowData()
			}
		}
		viewModelScope.launch {
			_accountUiState.collect {
				accountUiState = it
			}
		}
		viewModelScope.launch {
			userAccountDetails.collect { user ->
				user?.let {
					userId = user.id
				}
			}
		}
	}

	private fun fetchMovieData() {
		viewModelScope.launch {
			accountUseCase.execute(
				AccountUseCase.Request(accountId, MediaType.Movies)
			)
				.map { converter.convertToAccountMovieData(it) }
				.collectLatest { data ->
					_accountUiState.update { it.copy(moviesData = data) }
				}
		}
	}

	private fun fetchShowData() {
		viewModelScope.launch {
			accountUseCase.execute(
				AccountUseCase.Request(accountId, MediaType.Shows)
			)
				.map { converter.convertToAccountTvShowsData(it) }
				.collectLatest { data ->
					_accountUiState.update { it.copy(showsData = data) }
				}
		}
	}

	fun accountActions(action: AccountActions?) {
		when (action) {
			is AccountActions.UnFavoriteMedia -> removeFromFavorite(action.mediaId, action.mediaType)
			is AccountActions.RemoveFromWatchlist -> removeFromWatchlist(action.mediaId, action.mediaType)
			is AccountActions.DeleteMediaRating -> deleteMediaRating(action.mediaId, action.mediaType)
			AccountActions.ReloadMedia -> reloadMedia()
			else -> _accountUiState.update { it.copy(response = null) }
		}
	}

	private fun removeFromFavorite(
		mediaId: Int,
		mediaType: String
	) {
		mediaUtil.favoriteMedia(
			accountId = userId,
			mediaId = mediaId,
			mediaType = mediaType,
			favorite = false,
			result = { data ->
				_accountUiState.update { it.copy(response = data) }
			}
		)
	}

	private fun removeFromWatchlist(
		mediaId: Int,
		mediaType: String
	) {
		mediaUtil.watchlistMedia(
			accountId = userId,
			mediaId = mediaId,
			mediaType = mediaType,
			watchlist = false,
			result = { data ->
				_accountUiState.update { it.copy(response = data) }
			}
		)
	}

	private fun deleteMediaRating(
		mediaId: Int,
		mediaType: String
	) {
		viewModelScope.launch {
			mediaUtilUseCase.execute(
				MediaUtilUseCase.Request(
					mediaType = mediaType,
					mediaActions = MediaActions.DeleteRating(mediaId)
				)
			)
				.map { converter.convertResponse(it) }
				.collect { data ->
					_accountUiState.update { it.copy(response = data) }
				}
		}
	}

	private fun reloadMedia() {
		_accountUiState.update { it.copy(moviesData = null, showsData = null) }
		fetchMovieData(); fetchShowData()
	}

}