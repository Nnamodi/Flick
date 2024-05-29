package com.roland.android.flick.ui.screens.settings

import com.roland.android.domain.repository.AutoStreamOptions
import com.roland.android.domain.repository.ThemeOptions

sealed class SettingsActions {

	data class ToggleTheme(val theme: ThemeOptions) : SettingsActions()

	object ToggleAutoDataReload : SettingsActions()

	data class SetAutoStreamTrailers(val autoStream: AutoStreamOptions) : SettingsActions()

	object Logout : SettingsActions()

}
