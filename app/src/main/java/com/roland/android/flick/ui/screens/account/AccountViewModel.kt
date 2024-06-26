package com.roland.android.flick.ui.screens.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.AccountUseCase
import com.roland.android.domain.usecase.MediaCategory
import com.roland.android.flick.models.accountSessionId
import com.roland.android.flick.models.updateAccountDetails
import com.roland.android.flick.models.updatedMediaCategory
import com.roland.android.flick.models.userAccountDetails
import com.roland.android.flick.state.AccountUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.state.autoReloadData
import com.roland.android.flick.utils.MediaUtil
import com.roland.android.flick.utils.ResponseConverter
import com.roland.android.flick.utils.network.NetworkConnectivity
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
	private val networkConnectivity: NetworkConnectivity,
	private val converter: ResponseConverter,
	private val mediaUtil: MediaUtil
) : ViewModel() {

	private val _accountUiState = MutableStateFlow(AccountUiState())
	var accountUiState by mutableStateOf(_accountUiState.value); private set
	private var sessionId by mutableStateOf("")
	private var userId by mutableIntStateOf(0)
	var userLoggedIn by mutableStateOf(false); private set
	private var shouldAutoReloadData by mutableStateOf(true)
	private var shouldUpdateAccountDetails by mutableStateOf(false)

	init {
		viewModelScope.launch {
			updateAccountDetails.collect {
				shouldUpdateAccountDetails = it
			}
		}
		viewModelScope.launch {
			userAccountDetails.collect { account ->
				userLoggedIn = account?.id != 0
				if (account == null) return@collect
				userId = account.id
				_accountUiState.update { it.copy(accountDetails = account) }
				if (!userLoggedIn) return@collect
				reloadMedia()
			}
		}
		viewModelScope.launch {
			accountSessionId.collect {
				sessionId = it ?: ""
			}
		}
		viewModelScope.launch {
			_accountUiState.collect {
				accountUiState = it
			}
		}
		viewModelScope.launch {
			updatedMediaCategory.collectLatest { category ->
				when (category) {
					null -> return@collectLatest
					MediaCategory.Watchlisted -> fetchWatchlistMedia()
					MediaCategory.Favorited -> fetchFavoritedMedia()
					MediaCategory.Rated -> fetchRatedMedia()
				}
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
					((accountUiState.favoritedMedia !is State.Error) &&
					(accountUiState.watchlistedMedia !is State.Error) &&
					(accountUiState.ratedMedia !is State.Error))) return@collect
				reloadMedia()
			}
		}
	}

	private fun fetchWatchlistMedia() {
		viewModelScope.launch {
			accountUseCase.execute(
				AccountUseCase.Request(userId, sessionId, MediaCategory.Watchlisted)
			)
				.map { converter.convertToWatchlistedMedia(it) }
				.collectLatest { data ->
					if (accountUiState.watchlistedMedia !is State.Success) {
						_accountUiState.update { it.copy(watchlistedMedia = data) }
					}
					if (data !is State.Success) return@collectLatest
					_accountUiState.update { it.copy(watchlistedMedia = data) }
				}
		}
	}

	private fun fetchFavoritedMedia() {
		viewModelScope.launch {
			accountUseCase.execute(
				AccountUseCase.Request(userId, sessionId, MediaCategory.Favorited)
			)
				.map { converter.convertToFavoritedMedia(it) }
				.collectLatest { data ->
					if (accountUiState.favoritedMedia !is State.Success) {
						_accountUiState.update { it.copy(favoritedMedia = data) }
					}
					if (data !is State.Success) return@collectLatest
					_accountUiState.update { it.copy(favoritedMedia = data) }
				}
		}
	}

	private fun fetchRatedMedia() {
		viewModelScope.launch {
			accountUseCase.execute(
				AccountUseCase.Request(userId, sessionId, MediaCategory.Rated)
			)
				.map { converter.convertToRatedMedia(it) }
				.collectLatest { data ->
					if (accountUiState.ratedMedia !is State.Success) {
						_accountUiState.update { it.copy(ratedMedia = data) }
					}
					if (data !is State.Success) return@collectLatest
					_accountUiState.update { it.copy(ratedMedia = data) }
				}
		}
	}

	fun accountActions(action: AccountActions?) {
		action?.let { _accountUiState.update { it.copy(response = null) } }
		when (action) {
			is AccountActions.UnFavoriteMedia -> {
				mediaUtil.favoriteMedia(
					accountId = userId,
					sessionId = sessionId,
					mediaId = action.mediaId,
					mediaType = action.mediaType,
					favorite = false,
					result = { data ->
						_accountUiState.update { it.copy(response = data) }
					}
				)
			}
			is AccountActions.RemoveFromWatchlist -> {
				mediaUtil.watchlistMedia(
					accountId = userId,
					sessionId = sessionId,
					mediaId = action.mediaId,
					mediaType = action.mediaType,
					watchlist = false,
					result = { data ->
						_accountUiState.update { it.copy(response = data) }
					}
				)
			}
			is AccountActions.DeleteMediaRating -> {
				mediaUtil.deleteMediaRating(
					sessionId = sessionId,
					mediaId = action.mediaId,
					mediaType = action.mediaType,
					result = { data ->
						_accountUiState.update { it.copy(response = data) }
					}
				)
			}
			AccountActions.ReloadMedia -> reloadMedia()
			null -> _accountUiState.update { it.copy(response = null) }
		}
	}

	private fun reloadMedia() {
		if (shouldUpdateAccountDetails) {
			updateAccountDetails.value = false; return
		}
		_accountUiState.update { it.copy(watchlistedMedia = null, favoritedMedia = null, ratedMedia = null) }
		fetchWatchlistMedia(); fetchFavoritedMedia(); fetchRatedMedia()
	}

}