package com.roland.android.data_repository.data_source.local

import com.roland.android.domain.repository.AutoStreamOptions
import com.roland.android.domain.repository.ThemeOptions
import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {

	fun getTheme(): Flow<ThemeOptions>

	suspend fun setTheme(themeOption: ThemeOptions)

	fun getAutoDataReload(): Flow<Boolean>

	suspend fun setAutoDataReload(autoReload: Boolean)

	fun getAutoStreamTrailers(): Flow<AutoStreamOptions>

	suspend fun setAutoStreamTrailers(autoStreamOption: AutoStreamOptions)

}