package com.roland.android.flick.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.flick.R
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.ROUNDED_EDGE
import com.roland.android.flick.utils.Constants.SERIES
import com.roland.android.flick.utils.HomeScreenActions

@Composable
fun ToggleButton(
	selectedOption: String,
	modifier: Modifier = Modifier,
	onClick: (HomeScreenActions) -> Unit
) {
	Row(modifier) {
		Box(
			modifier = Modifier
				.clip(
					RoundedCornerShape(
						topStart = ROUNDED_EDGE,
						bottomStart = ROUNDED_EDGE
					)
				)
				.background(rememberBackgroundColor(selectedOption == MOVIES))
				.border(
					width = 2.dp,
					color = rememberBorderColor(selectedOption == MOVIES),
					shape = RoundedCornerShape(
						topStart = ROUNDED_EDGE,
						bottomStart = ROUNDED_EDGE
					)
				)
				.clickable { onClick(HomeScreenActions.ToggleCategory(MOVIES)) },
			contentAlignment = Alignment.Center
		) {
			ToggleButtonItem(
				text = stringResource(R.string.movies),
				textColor = rememberBorderColor(selectedOption == MOVIES)
			)
		}
		Box(
			modifier = Modifier
				.clip(
					RoundedCornerShape(
						topEnd = ROUNDED_EDGE,
						bottomEnd = ROUNDED_EDGE
					)
				)
				.background(rememberBackgroundColor(selectedOption == SERIES))
				.border(
					width = 2.dp,
					color = rememberBorderColor(selectedOption == SERIES),
					shape = RoundedCornerShape(
						topEnd = ROUNDED_EDGE,
						bottomEnd = ROUNDED_EDGE
					)
				)
				.clickable { onClick(HomeScreenActions.ToggleCategory(SERIES)) },
			contentAlignment = Alignment.Center
		) {
			ToggleButtonItem(
				text = stringResource(R.string.series),
				textColor = rememberBorderColor(selectedOption == SERIES)
			)
		}
	}
}

@Composable
private fun ToggleButtonItem(text: String, textColor: Color) {
	Text(
		text = text,
		modifier = Modifier.padding(20.dp, 6.dp),
		color = textColor,
		fontWeight = FontWeight.Bold,
		fontSize = 16.sp
	)
}

@Composable
private fun rememberBackgroundColor(selected: Boolean) = if (selected) {
	MaterialTheme.colorScheme.primaryContainer
} else MaterialTheme.colorScheme.background

@Composable
private fun rememberBorderColor(selected: Boolean) = if (selected) {
	MaterialTheme.colorScheme.onPrimaryContainer
} else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)

@Preview(showBackground = true)
@Composable
fun ToggleButtonPreview() {
	FlickTheme {
		var selectedOption by remember { mutableStateOf(MOVIES) }

		ToggleButton(selectedOption) {
			selectedOption = if (selectedOption == MOVIES) SERIES else MOVIES
		}
	}
}