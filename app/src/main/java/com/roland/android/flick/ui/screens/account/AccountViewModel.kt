package com.roland.android.flick.ui.screens.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.AccountUseCase
import com.roland.android.domain.usecase.MediaType
import com.roland.android.flick.models.userAccountDetails
import com.roland.android.flick.models.userAccountId
import com.roland.android.flick.state.AccountUiState
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
	private val accountUseCase: AccountUseCase,
	private val converter: ResponseConverter
) : ViewModel() {

	private val _accountUiState = MutableStateFlow(AccountUiState())
	var accountUiState by mutableStateOf(_accountUiState.value); private set
	private var accountId by mutableStateOf("")
	var userLoggedIn by mutableStateOf(false); private set

	init {
		viewModelScope.launch {
			userAccountDetails.collect { data ->
				if (data == null) return@collect
				_accountUiState.update { it.copy(accountDetails = data) }
				fetchMovieData(); fetchShowData()
			}
		}
		viewModelScope.launch {
			userAccountId.collect {
				accountId = it
				userLoggedIn = it.isNotEmpty()
			}
		}
		viewModelScope.launch {
			_accountUiState.collect {
				accountUiState = it
			}
		}
	}

	private fun fetchMovieData() {
		viewModelScope.launch {
			accountUseCase.execute(
				AccountUseCase.Request(accountId, MediaType.Movies)
			)
				.map { converter.convertToAccountMovieData(it) }
				.collect { data ->
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
				.collect { data ->
					_accountUiState.update { it.copy(showsData = data) }
				}
		}
	}

}