package com.roland.android.flick.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.roland.android.flick.ui.components.TopBar
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.settings.dialog.ThemesChooserDialog
import com.roland.android.flick.ui.theme.FlickTheme

@Composable
fun SettingsScreen(
	uiState: SettingsUiState,
	actions: (SettingsActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val openThemeDialog = remember { mutableStateOf(false) }

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
				.verticalScroll(rememberScrollState()),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Container(header = stringResource(R.string.display)) {
				OptionBox(
					label = stringResource(R.string.theme),
					subLabel = when (uiState.theme) {
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
							checked = uiState.autoReloadData,
							onCheckedChange = { actions(SettingsActions.ToggleAutoDataReload) }
						)
					},
					onClick = { actions(SettingsActions.ToggleAutoDataReload) }
				)
				OptionBox(
					label = stringResource(R.string.auto_play_trailers),
					subLabel = when (uiState.autoStreamTrailers) {
						Always -> stringResource(R.string.always)
						Wifi -> stringResource(R.string.wifi)
						Never -> stringResource(R.string.never)
					},
					onClick = {}
				)
			}
		}
	}

	if (openThemeDialog.value) {
		ThemesChooserDialog(
			selectedTheme = uiState.theme,
			onSelected = actions,
			closeDialog = { openThemeDialog.value = false }
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
				fontWeight = FontWeight.Normal,
				style = MaterialTheme.typography.titleLarge
			)
			subLabel?.let {
				Text(
					text = it,
					modifier = Modifier.alpha(0.5f),
					fontWeight = FontWeight.Normal,
					lineHeight = 16.sp,
					style = MaterialTheme.typography.titleMedium
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