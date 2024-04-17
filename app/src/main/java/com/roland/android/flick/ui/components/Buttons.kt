package com.roland.android.flick.ui.components

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.roland.android.flick.utils.ChromeTabUtils
import com.roland.android.flick.utils.Constants.IMDB_BASE_URL
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.ROUNDED_EDGE
import com.roland.android.flick.utils.Constants.SERIES
import com.roland.android.flick.utils.Constants.YOUTUBE_VIDEO_BASE_URL
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.bounceClickable
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.delay

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
fun SignUpButton(
	loading: Boolean,
	failed: Boolean,
	completed: Boolean,
	onClick: () -> Unit
) {
	val requestFailed = rememberSaveable(failed) { mutableStateOf(failed) }

	Column(Modifier.padding(40.dp)) {
		when {
			loading -> {
				CircularProgressIndicator()
			}
			requestFailed.value -> {
				Icon(
					imageVector = Icons.Rounded.Cancel,
					contentDescription = null,
					modifier = Modifier.size(50.dp),
					tint = Color.Red
				)
			}
			completed -> {
				Icon(
					imageVector = Icons.Rounded.CheckCircle,
					contentDescription = null,
					modifier = Modifier.size(50.dp),
					tint = Color.Green
				)
			}
			else -> {
				Button(
					onClick = onClick,
					modifier = Modifier.fillMaxWidth()
				) {
					Text(text = stringResource(R.string.sign_up))
				}
			}
		}
	}

	LaunchedEffect(failed) {
		if (!requestFailed.value) return@LaunchedEffect
		delay(2000)
		requestFailed.value = false
	}
}

@Composable
fun OpenWithButton(
	imdbId: String?,
	trailerKey: String?
) {
	val context = LocalContext.current
	val chromeTabUtils = ChromeTabUtils(context)
	val clicked = rememberSaveable { mutableStateOf(false) }
	val backgroundTint by animateColorAsState(
		targetValue = if (clicked.value) Color.Black.copy(alpha = 0.5f) else Color.Transparent,
		label = "background tint"
	)

	Row(
		modifier = Modifier
			.animateContentSize()
			.clip(MaterialTheme.shapes.large)
			.background(backgroundTint)
			.padding(10.dp),
		horizontalArrangement = Arrangement.spacedBy(20.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Column(
			modifier = Modifier.bounceClickable { clicked.value = !clicked.value },
			verticalArrangement = Arrangement.spacedBy(4.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Icon(Icons.Rounded.OpenInNew, stringResource(R.string.open_with))
			Text(stringResource(R.string.open_with))
		}
		if (clicked.value) {
			OpenButtonOptions.values().forEach { button ->
				Image(
					painter = painterResource(button.imageRes),
					contentDescription = stringResource(button.nameRes),
					modifier = Modifier
						.size(32.dp)
						.clip(MaterialTheme.shapes.large)
						.clickable {
							val url = if (button == OpenButtonOptions.IMDB) imdbId else trailerKey
							url?.let {
								val baseUrl =
									if (button == OpenButtonOptions.IMDB) IMDB_BASE_URL else YOUTUBE_VIDEO_BASE_URL
								chromeTabUtils.launchUrl(baseUrl + it)
							} ?: kotlin.run {
								showToast(context)
							}
						}
				)
			}
		}
	}
}

fun showToast(
	context: Context,
	@StringRes textRes: Int = R.string.url_unavailable
) {
	Toast
		.makeText(
			context,
			context.getString(textRes),
			Toast.LENGTH_SHORT
		)
		.show()
}

private enum class OpenButtonOptions(
	@StringRes val nameRes: Int,
	@DrawableRes val imageRes: Int
) {
	YouTube(
		nameRes = R.string.open_on_youtube,
		imageRes = R.drawable.youtube_logo
	),
	IMDB(
		nameRes = R.string.open_on_imdb,
		imageRes = R.drawable.imdb_logo
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