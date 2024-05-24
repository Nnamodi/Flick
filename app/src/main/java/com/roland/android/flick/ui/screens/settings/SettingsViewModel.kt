package com.roland.android.flick.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.repository.AutoStreamOptions
import com.roland.android.domain.repository.ThemeOptions
import com.roland.android.domain.usecase.SettingsRequest
import com.roland.android.domain.usecase.SettingsUseCase
import com.roland.android.flick.state.SettingsUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.state.autoReloadData
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
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
			_settingsUiState.collect {
				settingsUiState = it
			}
		}
	}

	fun settingsActions(action: SettingsActions) {
		when (action) {
			is SettingsActions.ToggleTheme -> toggleTheme(action.theme)
			SettingsActions.ToggleAutoDataReload -> setAutoDataReload(!settingsUiState.autoReloadData)
			is SettingsActions.SetAutoStreamTrailers -> setAutoStreamTrailer(action.autoStream)
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
					_settingsUiState.update { it.copy(autoStreamTrailers = response.data) }
				}
		}
	}

}