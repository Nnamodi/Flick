package com.roland.android.data_local.data_source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.roland.android.data_repository.data_source.local.SettingsDataSource
import com.roland.android.domain.repository.AutoStreamOptions
import com.roland.android.domain.repository.ThemeOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal val THEME_KEY = stringPreferencesKey("theme_key")
internal val AUTO_RELOAD_KEY = booleanPreferencesKey("auto_reload_key")
internal val AUTO_STREAM_KEY = stringPreferencesKey("auto_stream_key")

class SettingsDataSourceImpl @Inject constructor(
	private val dataStore: DataStore<Preferences>
) : SettingsDataSource {

	override fun getTheme(): Flow<ThemeOptions> {
		return dataStore.data.map {
			val value = it[THEME_KEY] ?: ThemeOptions.Dark.name
			ThemeOptions.valueOf(value)
		}
	}

	override suspend fun setTheme(themeOption: ThemeOptions) {
		dataStore.edit {
			it[THEME_KEY] = themeOption.name
		}
	}

	override fun getAutoDataReload(): Flow<Boolean> {
		return dataStore.data.map {
			it[AUTO_RELOAD_KEY] ?: true
		}
	}

	override suspend fun setAutoDataReload(autoReload: Boolean) {
		dataStore.edit {
			it[AUTO_RELOAD_KEY] = autoReload
		}
	}

	override fun getAutoStreamTrailers(): Flow<AutoStreamOptions> {
		return dataStore.data.map {
			val value = it[AUTO_STREAM_KEY] ?: AutoStreamOptions.Always.name
			AutoStreamOptions.valueOf(value)
		}
	}

	override suspend fun setAutoStreamTrailers(autoStreamOption: AutoStreamOptions) {
		dataStore.edit {
			it[AUTO_STREAM_KEY] = autoStreamOption.name
		}
	}

}