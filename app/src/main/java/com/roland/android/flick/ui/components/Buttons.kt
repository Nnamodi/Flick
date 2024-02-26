package com.roland.android.flick.ui.components

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.flick.R
import com.roland.android.flick.ui.screens.coming_soon.ComingSoonActions
import com.roland.android.flick.ui.screens.home.HomeActions
import com.roland.android.flick.ui.screens.search.SearchCategory
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.ROUNDED_EDGE
import com.roland.android.flick.utils.Constants.SERIES
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.bounceClickable
import com.roland.android.flick.utils.rememberWindowSize

@Composable
fun ToggleButton(
	selectedOption: String,
	modifier: Modifier = Modifier,
	onClick: (HomeActions) -> Unit
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
				.clickable { onClick(HomeActions.ToggleCategory(MOVIES)) },
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
				.clickable { onClick(HomeActions.ToggleCategory(SERIES)) },
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
fun ChipSet(
	modifier: Modifier = Modifier,
	selectedCategory: SearchCategory?,
	chips: Array<Chips> = Chips.values(),
	onValueChanged: (SearchCategory) -> Unit
) {
	val windowSize = rememberWindowSize()
	val chipSet: @Composable () -> Unit = {
		AssistChipSet(chips, onValueChanged, selectedCategory)
	}

	if (windowSize.width == WindowType.Landscape) {
		Column(
			modifier = modifier,
			verticalArrangement = Arrangement.SpaceAround,
			horizontalAlignment = Alignment.CenterHorizontally
		) { chipSet() }
	} else {
		Row(
			modifier = modifier,
			horizontalArrangement = Arrangement.SpaceAround
		) { chipSet() }
	}
}

@Composable
private fun AssistChipSet(
	chips: Array<Chips>,
	onValueChanged: (SearchCategory) -> Unit,
	selectedCategory: SearchCategory?,
) {
	chips.forEach { chip ->
		AssistChip(
			onClick = { onValueChanged(chip.category) },
			label = {
				Text(stringResource(chip.labelRes))
			},
			colors = AssistChipDefaults.assistChipColors(
				containerColor = rememberContainerColor(selectedCategory == chip.category),
				labelColor = rememberLabelColor(selectedCategory == chip.category)
			)
		)
	}
}

enum class Chips(
	@StringRes val labelRes: Int,
	val category: SearchCategory
) {
	All(R.string.all, SearchCategory.ALL),
	Movies(R.string.movies, SearchCategory.MOVIES),
	TvShows(R.string.series, SearchCategory.TV_SHOWS)
}

@Composable
fun CustomDropDown(
	selectedCategory: String,
	onCategorySelected: (ComingSoonActions) -> Unit
) {
	val currentCategory = if (selectedCategory == MOVIES) Chips.Movies else Chips.TvShows
	val nextCategory = if (selectedCategory == MOVIES) Chips.TvShows else Chips.Movies
	val expanded = rememberSaveable { mutableStateOf(false) }

	Column(
		modifier = Modifier
			.padding(vertical = 12.dp)
			.padding(end = 12.dp)
			.clip(MaterialTheme.shapes.extraLarge)
			.border(
				width = 1.dp,
				color = MaterialTheme.colorScheme.outline,
				shape = MaterialTheme.shapes.extraLarge
			)
			.animateContentSize()
	) {
		CustomDropDownItem(
			labelRes = currentCategory.labelRes,
			selected = true,
			expanded = expanded.value,
			onClick = {
				expanded.value = !expanded.value
				return@CustomDropDownItem
			}
		)
		if (expanded.value) {
			CustomDropDownItem(
				labelRes = nextCategory.labelRes,
				selected = false,
				expanded = expanded.value,
				onClick = {
					onCategorySelected(ComingSoonActions.ToggleCategory)
					expanded.value = false
				}
			)
		}
	}

	if (expanded.value) {
		BackHandler {
			expanded.value = false
		}
	}
}

@Composable
private fun CustomDropDownItem(
	@StringRes labelRes: Int,
	selected: Boolean,
	expanded: Boolean,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	Row(
		modifier = modifier
			.padding(8.dp, 6.dp)
			.bounceClickable { onClick() },
		verticalAlignment = Alignment.CenterVertically

	) {
		Text(text = stringResource(labelRes), Modifier.padding(end = 8.dp))
		Icon(
			imageVector = if (expanded) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
			contentDescription = null,
			modifier = Modifier.alpha(if (selected) 1f else 0f) // makes the icon present but invisible so the whole space is clickable
		)
	}
}

@Composable
private fun rememberBackgroundColor(selected: Boolean) = if (selected) {
	MaterialTheme.colorScheme.primaryContainer
} else MaterialTheme.colorScheme.background

@Composable
private fun rememberBorderColor(selected: Boolean) = if (selected) {
	MaterialTheme.colorScheme.onPrimaryContainer
} else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)

@Composable
private fun rememberContainerColor(selected: Boolean) = if (selected) {
	MaterialTheme.colorScheme.primaryContainer
} else Color.Transparent

@Composable
private fun rememberLabelColor(selected: Boolean) = if (selected) {
	MaterialTheme.colorScheme.onPrimaryContainer
} else MaterialTheme.colorScheme.onSurface

@Preview(showBackground = true)
@Composable
private fun ToggleButtonPreview() {
	FlickTheme {
		var selectedOption by remember { mutableStateOf(MOVIES) }

		ToggleButton(selectedOption) {
			selectedOption = if (selectedOption == MOVIES) SERIES else MOVIES
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun ChipSet() {
	FlickTheme {
		ChipSet(
			modifier = Modifier.fillMaxWidth(),
			selectedCategory = SearchCategory.MOVIES
		) {}
	}
}