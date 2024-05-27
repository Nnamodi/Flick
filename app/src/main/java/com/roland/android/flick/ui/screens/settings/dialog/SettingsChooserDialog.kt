package com.roland.android.flick.ui.screens.settings.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.domain.repository.AutoStreamOptions
import com.roland.android.domain.repository.AutoStreamOptions.Always
import com.roland.android.domain.repository.AutoStreamOptions.Never
import com.roland.android.domain.repository.AutoStreamOptions.Wifi
import com.roland.android.domain.repository.ThemeOptions
import com.roland.android.domain.repository.ThemeOptions.Dark
import com.roland.android.domain.repository.ThemeOptions.FollowSystem
import com.roland.android.domain.repository.ThemeOptions.Light
import com.roland.android.flick.R
import com.roland.android.flick.ui.screens.settings.SettingsActions

@Composable
fun ThemesChooserDialog(
	selectedTheme: ThemeOptions,
	onSelected: (SettingsActions) -> Unit,
	closeDialog: () -> Unit
) {
	ChooserDialog(
		title = stringResource(R.string.choose_theme),
		content = {
			ThemeOptions.values().forEach { theme ->
				Option(
					option = when (theme) {
						Dark -> stringResource(R.string.dark)
						Light -> stringResource(R.string.light)
						FollowSystem -> stringResource(R.string.follow_system)
					},
					selected = selectedTheme == theme,
					onSelect = {
						onSelected(SettingsActions.ToggleTheme(theme))
						closeDialog()
					}
				)
			}
		},
		closeDialog = closeDialog
	)
}

@Composable
fun AutoStreamChooserDialog(
	selectedOption: AutoStreamOptions,
	onSelected: (SettingsActions) -> Unit,
	closeDialog: () -> Unit
) {
	ChooserDialog(
		title = stringResource(R.string.choose_stream_option),
		content = {
			AutoStreamOptions.values().forEach { option ->
				Option(
					option = when (option) {
						Always -> stringResource(R.string.always)
						Wifi -> stringResource(R.string.wifi)
						Never -> stringResource(R.string.never)
					},
					selected = selectedOption == option,
					onSelect = {
						onSelected(SettingsActions.SetAutoStreamTrailers(option))
						closeDialog()
					}
				)
			}
		},
		closeDialog = closeDialog
	)
}

@Composable
private fun ChooserDialog(
	title: String,
	content: @Composable ColumnScope.() -> Unit,
	closeDialog: () -> Unit
) {
	AlertDialog(
		onDismissRequest = closeDialog,
		title = { Text(title) },
		text = {
			Column(Modifier.verticalScroll(rememberScrollState())) {
				content()
			}
		},
		confirmButton = {
			TextButton(onClick = closeDialog) {
				Text(text = stringResource(R.string.close), fontSize = 18.sp)
			}
		}
	)
}

@Composable
private fun Option(
	option: String,
	selected: Boolean,
	onSelect: () -> Unit
) {
	val color = if (selected) MaterialTheme.colorScheme.primary else AlertDialogDefaults.textContentColor

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clip(MaterialTheme.shapes.medium)
			.clickable { onSelect() }
			.padding(8.dp, 10.dp),
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = option,
			modifier = Modifier
				.weight(1f)
				.padding(vertical = 2.dp),
			color = color,
			fontSize = 20.sp,
			fontWeight = FontWeight.Normal,
			style = MaterialTheme.typography.titleMedium
		)
		if (selected) {
			Icon(
				imageVector = Icons.Rounded.Done,
				contentDescription = stringResource(R.string.option_selected, option),
				tint = color
			)
		}
	}
}