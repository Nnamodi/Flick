package com.roland.android.flick.ui.screens.details

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.flick.R
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.bounceClickable

@Composable
fun SeasonSelectionSheet(
	showSheet: Boolean,
	seriesId: Int,
	selectedSeasonNumber: Int,
	numberOfSeasons: Int,
	onSeasonSelected: (DetailsRequest) -> Unit,
	closeSheet: (Int?) -> Unit
) {
	AnimatedVisibility(
		visible = showSheet,
		enter = slideInVertically(tween(700)) { it },
		exit = slideOutVertically(tween(400)) { it },
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f)),
			verticalArrangement = Arrangement.Bottom,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			val lazyListState = rememberLazyListState()

			LazyColumn(
				modifier = Modifier.weight(1f),
				state = lazyListState,
				contentPadding = PaddingValues(top = 50.dp),
				reverseLayout = true
			) {
				val seasons = (1..numberOfSeasons).toList()

				items(numberOfSeasons) { index ->
					SeasonOption(
						seasonNumber = seasons[index],
						selectedSeasonNumber = selectedSeasonNumber,
						seriesId = seriesId,
						onSeasonSelected = { onSeasonSelected(it); closeSheet(index + 1) }
					)
				}
			}
			Icon(
				imageVector = Icons.Rounded.Cancel,
				contentDescription = stringResource(androidx.compose.ui.R.string.close_sheet),
				modifier = Modifier
					.size(100.dp)
					.bounceClickable { closeSheet(null) }
					.padding(vertical = 10.dp),
				tint = MaterialTheme.colorScheme.onBackground
			)

			LaunchedEffect(true) {
				val itemIndex = (selectedSeasonNumber - 3).coerceAtLeast(0)
				lazyListState.scrollToItem(itemIndex)
			}
		}
	}

	if (showSheet) {
		BackHandler { closeSheet(null) }
	}
}

@Composable
private fun SeasonOption(
	seasonNumber: Int,
	selectedSeasonNumber: Int,
	seriesId: Int,
	onSeasonSelected: (DetailsRequest) -> Unit,
) {
	val optionIsSelected = seasonNumber == selectedSeasonNumber

	Text(
		text = stringResource(R.string.season_number, seasonNumber),
		modifier = Modifier
			.fillMaxWidth()
			.bounceClickable(
				enabled = seasonNumber != selectedSeasonNumber,
				indication = LocalIndication.current
			) {
				val request =
					DetailsRequest.GetSeasonDetails(seriesId, seasonNumber)
				onSeasonSelected(request)
			}
			.padding(vertical = 14.dp)
			.padding(6.dp),
		color = if (optionIsSelected) MaterialTheme.colorScheme.surfaceTint else Color.Unspecified,
		fontWeight = FontWeight.Bold,
		fontSize = if (optionIsSelected) 32.sp else 26.sp,
		textAlign = TextAlign.Center
	)
}

@Preview(showBackground = true)
@Composable
private fun SeasonSelectionSheetPreview() {
	FlickTheme(true) {
		val openSheet = remember { mutableStateOf(true) }

		Surface(
			modifier = Modifier.fillMaxSize(),
			onClick = { openSheet.value = true }
		) {
			SeasonSelectionSheet(
				showSheet = openSheet.value,
				seriesId = 156465,
				selectedSeasonNumber = 3,
				numberOfSeasons = 14,
				onSeasonSelected = {}
			) { openSheet.value = false }
		}
	}
}