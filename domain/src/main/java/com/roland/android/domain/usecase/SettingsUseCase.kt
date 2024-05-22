package com.roland.android.domain.usecase

import com.roland.android.domain.repository.AutoStreamOptions
import com.roland.android.domain.repository.SettingsRepository
import com.roland.android.domain.repository.ThemeOptions
import com.roland.android.domain.usecase.SettingsRequest.AutoReloadData
import com.roland.android.domain.usecase.SettingsRequest.AutoStreamTrailers
import com.roland.android.domain.usecase.SettingsRequest.ToggleTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
	configuration: Configuration,
	private val settingsRepository: SettingsRepository
) : UseCase<SettingsUseCase.Request, SettingsUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> {
		return when (request.settingsRequest) {
			ToggleTheme -> {
				settingsRepository.toggleTheme(request.selectedTheme).map { Response(theme = it) }
			}
			AutoReloadData -> {
				settingsRepository.autoDataReload(request.shouldAutoReloadData).map { Response(autoReloadData = it) }
			}
			AutoStreamTrailers -> {
				settingsRepository.autoStreamTrailers(request.shouldAutoStreamTrailers).map { Response(autoStreamTrailers = it) }
			}
		}
	}

	data class Request(
		val selectedTheme: ThemeOptions? = null,
		val shouldAutoReloadData: Boolean? = null,
		val shouldAutoStreamTrailers: AutoStreamOptions? = null,
		val settingsRequest: SettingsRequest
	) : UseCase.Request

	data class Response(
		val theme: ThemeOptions = ThemeOptions.Dark,
		val autoReloadData: Boolean = true,
		val autoStreamTrailers: AutoStreamOptions = AutoStreamOptions.Always
	) : UseCase.Response

}

enum class SettingsRequest {
	ToggleTheme,
	AutoReloadData,
	AutoStreamTrailers
}
