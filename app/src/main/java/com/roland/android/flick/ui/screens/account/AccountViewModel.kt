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
import com.roland.android.flick.models.accountMediaUpdated
import com.roland.android.flick.models.userAccountDetails
import com.roland.android.flick.models.userAccountId
import com.roland.android.flick.state.AccountUiState
import com.roland.android.flick.state.State
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
		viewModelScope.launch {
			accountMediaUpdated.collectLatest {
				if (!it) return@collectLatest
				fetchMovieData(); fetchShowData()
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
					if (accountUiState.moviesData !is State.Success) {
						_accountUiState.update { it.copy(moviesData = data) }
					}
					if (data !is State.Success) return@collectLatest
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
					if (accountUiState.showsData !is State.Success) {
						_accountUiState.update { it.copy(showsData = data) }
					}
					if (data !is State.Success) return@collectLatest
					_accountUiState.update { it.copy(showsData = data) }
				}
		}
	}

	fun accountActions(action: AccountActions?) {
		action?.let { _accountUiState.update { it.copy(response = null) } }
		when (action) {
			is AccountActions.UnFavoriteMedia -> {
				mediaUtil.favoriteMedia(
					accountId = userId,
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
					mediaId = action.mediaId,
					mediaType = action.mediaType,
					watchlist = false,
					result = { data ->
						_accountUiState.update { it.copy(response = data) }
					}
				)
			}
			is AccountActions.DeleteMediaRating -> deleteMediaRating(
				mediaId = action.mediaId,
				mediaType = action.mediaType
			)
			AccountActions.ReloadMedia -> reloadMedia()
			null -> _accountUiState.update { it.copy(response = null) }
		}
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