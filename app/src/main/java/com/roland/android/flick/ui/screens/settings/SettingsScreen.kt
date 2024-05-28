package com.roland.android.flick.ui.screens.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.domain.repository.AutoStreamOptions.Always
import com.roland.android.domain.repository.AutoStreamOptions.Never
import com.roland.android.domain.repository.AutoStreamOptions.Wifi
import com.roland.android.domain.repository.ThemeOptions.Dark
import com.roland.android.domain.repository.ThemeOptions.FollowSystem
import com.roland.android.domain.repository.ThemeOptions.Light
import com.roland.android.flick.R
import com.roland.android.flick.state.SettingsUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.Snackbar
import com.roland.android.flick.ui.components.TopBar
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScaffold
import com.roland.android.flick.ui.screens.settings.dialog.AutoStreamChooserDialog
import com.roland.android.flick.ui.screens.settings.dialog.ThemesChooserDialog
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.ChromeTabUtils
import com.roland.android.flick.utils.Constants.ACCOUNT_SETTINGS_URL
import com.roland.android.flick.utils.Constants.EDIT_PROFILE_URL

@Composable
fun SettingsScreen(
	uiState: SettingsUiState,
	action: (SettingsActions?) -> Unit,
	navigate: (Screens) -> Unit
) {
	val (theme, autoReloadData, autoStreamOption, userIsLoggedIn) = uiState
	val openThemeDialog = remember { mutableStateOf(false) }
	val openAutoStreamDialog = remember { mutableStateOf(false) }
	val chromeTabUtils = ChromeTabUtils(LocalContext.current)

	Scaffold(
		topBar = {
			TopBar(
				title = stringResource(R.string.settings),
				navigateUp = navigate
			)
		}
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.verticalScroll(rememberScrollState())
				.animateContentSize(),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			if (userIsLoggedIn) {
				Container(header = stringResource(R.string.account)) {
					OptionBox(
						label = stringResource(R.string.edit_profile),
						trailingContent = { Icon(Icons.Rounded.OpenInNew, null) },
						onClick = { chromeTabUtils.launchUrl(EDIT_PROFILE_URL) }
					)
					OptionBox(
						label = stringResource(R.string.account_settings),
						trailingContent = { Icon(Icons.Rounded.OpenInNew, null) },
						onClick = { chromeTabUtils.launchUrl(ACCOUNT_SETTINGS_URL) }
					)
				}
			}
			Container(header = stringResource(R.string.display)) {
				OptionBox(
					label = stringResource(R.string.theme),
					subLabel = when (theme) {
						Dark -> stringResource(R.string.dark)
						Light -> stringResource(R.string.light)
						FollowSystem -> stringResource(R.string.follow_system)
					},
					onClick = { openThemeDialog.value = true }
				)
			}
			Container(header = stringResource(R.string.data_saving)) {
				OptionBox(
					label = stringResource(R.string.auto_reload_data),
					subLabel = stringResource(R.string.auto_reload_desc),
					trailingContent = {
						Switch(
							checked = autoReloadData,
							onCheckedChange = { action(SettingsActions.ToggleAutoDataReload) }
						)
					},
					onClick = { action(SettingsActions.ToggleAutoDataReload) }
				)
				OptionBox(
					label = stringResource(R.string.auto_play_trailers),
					subLabel = when (autoStreamOption) {
						Always -> stringResource(R.string.always)
						Wifi -> stringResource(R.string.wifi)
						Never -> stringResource(R.string.never)
					},
					onClick = { openAutoStreamDialog.value = true }
				)
			}
		}
	}

	if (openThemeDialog.value) {
		ThemesChooserDialog(
			selectedTheme = theme,
			onSelected = action,
			closeDialog = { openThemeDialog.value = false }
		)
	}

	if (openAutoStreamDialog.value) {
		AutoStreamChooserDialog(
			selectedOption = autoStreamOption,
			onSelected = action,
			closeDialog = { openAutoStreamDialog.value = false }
		)
	}
}

@Composable
private fun Container(
	header: String,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	Column(modifier.fillMaxWidth()) {
		Divider()
		Text(
			text = header,
			modifier = Modifier
				.padding(20.dp, 14.dp)
				.alpha(0.7f),
			style = MaterialTheme.typography.titleMedium
		)
		content()
	}
}

@Composable
private fun OptionBox(
	label: String,
	modifier: Modifier = Modifier,
	subLabel: String? = null,
	trailingContent: @Composable () -> Unit = {},
	onClick: () -> Unit
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.clickable { onClick() }
			.padding(20.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Column(
			Modifier
				.weight(1f)
				.padding(end = 10.dp)
		) {
			Text(
				text = label,
				fontSize = 20.sp,
				style = MaterialTheme.typography.bodyLarge
			)
			subLabel?.let {
				Text(
					text = it,
					modifier = Modifier.alpha(0.5f),
					lineHeight = 16.sp,
					style = MaterialTheme.typography.bodyLarge
				)
			}
		}
		trailingContent()
	}
}

@Preview
@Composable
fun SettingsScreenPreview() {
	FlickTheme {
		SettingsScreen(SettingsUiState(), {}) {}
	}
}