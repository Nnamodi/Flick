package com.roland.android.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

	fun toggleTheme(themeOption: ThemeOptions?): Flow<ThemeOptions>

	fun autoDataReload(autoReload: Boolean?): Flow<Boolean>

	fun autoStreamTrailers(autoStreamOption: AutoStreamOptions?): Flow<AutoStreamOptions>

}

enum class ThemeOptions {
	Dark, Light, FollowSystem
}

enum class AutoStreamOptions {
	Always, Wifi, Never
}