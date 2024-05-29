package com.roland.android.flick.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.repository.AutoStreamOptions
import com.roland.android.domain.repository.ThemeOptions
import com.roland.android.domain.usecase.AuthRequest
import com.roland.android.domain.usecase.AuthUseCase
import com.roland.android.domain.usecase.SettingsRequest
import com.roland.android.domain.usecase.SettingsUseCase
import com.roland.android.flick.models.accountSessionId
import com.roland.android.flick.models.userAccountDetails
import com.roland.android.flick.models.userAccountId
import com.roland.android.flick.state.SettingsUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.state.autoReloadData
import com.roland.android.flick.state.autoStreamTrailersOption
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val authUseCase: AuthUseCase,
	private val settingsUseCase: SettingsUseCase,
	private val converter: ResponseConverter
) : ViewModel() {

	private val _settingsUiState = MutableStateFlow(SettingsUiState())
	var settingsUiState by mutableStateOf(_settingsUiState.value); private set

	init {
		// fetch saved data from local data source
		toggleTheme(null)
		setAutoDataReload(null)
		setAutoStreamTrailer(null)

		viewModelScope.launch {
			userAccountDetails.collect { user ->
				_settingsUiState.update { it.copy(userIsLoggedIn = user?.id != 0) }
			}
		}
		viewModelScope.launch {
			_settingsUiState.collect {
				settingsUiState = it
			}
		}
	}

	fun onActivityResumed(resumed: Boolean) {
		_settingsUiState.update { it.copy(activityResumed = resumed) }
	}

	fun settingsActions(action: SettingsActions?) {
		if (action == null) {
			_settingsUiState.update { it.copy(logoutResponse = null) }
			return
		}
		when (action) {
			is SettingsActions.ToggleTheme -> toggleTheme(action.theme)
			SettingsActions.ToggleAutoDataReload -> setAutoDataReload(!settingsUiState.autoReloadData)
			is SettingsActions.SetAutoStreamTrailers -> setAutoStreamTrailer(action.autoStream)
			SettingsActions.Logout -> logout()
		}
	}

	private fun toggleTheme(theme: ThemeOptions?) {
		viewModelScope.launch {
			settingsUseCase.execute(
				SettingsUseCase.Request(
					selectedTheme = theme,
					settingsRequest = SettingsRequest.ToggleTheme
				)
			)
				.map { converter.convertThemeResponse(it) }
				.collect { response ->
					if (response !is State.Success) return@collect
					_settingsUiState.update { it.copy(theme = response.data) }
				}
		}
	}

	private fun setAutoDataReload(autoReload: Boolean?) {
		viewModelScope.launch {
			settingsUseCase.execute(
				SettingsUseCase.Request(
					shouldAutoReloadData = autoReload,
					settingsRequest = SettingsRequest.AutoReloadData
				)
			)
				.map { converter.convertAutoReloadResponse(it) }
				.collect { response ->
					if (response !is State.Success) return@collect
					_settingsUiState.update { it.copy(autoReloadData = response.data) }
					autoReloadData.value = response.data
				}
		}
	}

	private fun setAutoStreamTrailer(autoStream: AutoStreamOptions?) {
		viewModelScope.launch {
			settingsUseCase.execute(
				SettingsUseCase.Request(
					shouldAutoStreamTrailers = autoStream,
					settingsRequest = SettingsRequest.AutoStreamTrailers
				)
			)
				.map { converter.convertAutoStreamResponse(it) }
				.collect { response ->
					if (response !is State.Success) return@collect
					_settingsUiState.update { it.copy(autoStreamOption = response.data) }
					autoStreamTrailersOption.value = response.data
				}
		}
	}

	private fun logout() {
		viewModelScope.launch {
			delay(2000)
			authUseCase.execute(
				AuthUseCase.Request(authRequest = AuthRequest.Logout)
			)
				.map { converter.convertResponseData(it) }
				.collect { data ->
					_settingsUiState.update { it.copy(logoutResponse = data) }
					if (data !is State.Success) return@collect
					accountSessionId.value = null
					userAccountDetails.value = null
					userAccountId.value = ""
				}
		}
	}

}