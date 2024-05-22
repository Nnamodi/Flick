package com.roland.android.data_repository.repository

import com.roland.android.data_repository.data_source.local.SettingsDataSource
import com.roland.android.domain.repository.AutoStreamOptions
import com.roland.android.domain.repository.SettingsRepository
import com.roland.android.domain.repository.ThemeOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
	private val coroutineScope: CoroutineScope,
	private val settingsDataSource: SettingsDataSource
) : SettingsRepository {

	override fun toggleTheme(themeOption: ThemeOptions?): Flow<ThemeOptions> {
		coroutineScope.launch {
			themeOption?.let { settingsDataSource.setTheme(it) }
		}
		return settingsDataSource.getTheme()
	}

	override fun autoDataReload(autoReload: Boolean?): Flow<Boolean> {
		coroutineScope.launch {
			autoReload?.let { settingsDataSource.setAutoDataReload(it) }
		}
		return settingsDataSource.getAutoDataReload()
	}

	override fun autoStreamTrailers(autoStreamOption: AutoStreamOptions?): Flow<AutoStreamOptions> {
		coroutineScope.launch {
			autoStreamOption?.let { settingsDataSource.setAutoStreamTrailers(it) }
		}
		return settingsDataSource.getAutoStreamTrailers()
	}

}