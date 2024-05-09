package com.roland.android.flick.ui.screens.details.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.Series
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.flick.R
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.screens.details.MovieDetailsActions
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.SERIES
import com.roland.android.flick.utils.bounceClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateMediaDialog(
	movie: MovieDetails?,
	series: Series?,
	response: State<Response>?,
	onMediaRated: (MovieDetailsActions) -> Unit,
	closeSheet: () -> Unit
) {
	val mediaType = if (movie == null) SERIES else MOVIES
	val requestLoading = remember { mutableStateOf(false) }
	val mediaRating = (movie?.voteAverage ?: series?.voteAverage)?.toInt()
	var rateValue by rememberSaveable { mutableIntStateOf(mediaRating ?: 0) }
	var rateValueChanged by rememberSaveable { mutableStateOf(false) }

	AlertDialog(
		onDismissRequest = { if (!requestLoading.value) closeSheet() },
		modifier = Modifier
			.clip(AlertDialogDefaults.shape)
			.background(AlertDialogDefaults.containerColor)
	) {
		Column(
			modifier = Modifier.padding(20.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = stringResource(if (movie == null) R.string.rate_series else R.string.rate_movie),
				fontWeight = FontWeight.Bold,
				style = MaterialTheme.typography.titleLarge
			)
			MediaRatingBar(
				rateValue = rateValue,
				rateValueChanged = rateValueChanged,
				onRateValueChange = { rateValue = it; rateValueChanged = true }
			)
			Text(
				text = stringResource(R.string.your_rating, rateValue),
				modifier = Modifier
					.padding(top = 20.dp, bottom = 40.dp)
					.alpha(if (rateValueChanged) 1f else 0f)
			)
			Button(
				onClick = {
					requestLoading.value = true
					val rateAction = MovieDetailsActions.RateMedia(
						mediaId = (movie?.id ?: series?.id)!!,
						mediaType = mediaType,
						rateValue = rateValue.toFloat()
					)
					onMediaRated(rateAction)
				},
				modifier = Modifier.fillMaxWidth(0.7f),
				enabled = rateValueChanged && !requestLoading.value
			) {
				if (requestLoading.value) {
					CircularProgressIndicator(
						modifier = Modifier
							.padding(bottom = 4.dp)
							.size(20.dp),
						strokeWidth = 2.dp
					)
				} else {
					Text(stringResource(R.string.rate).uppercase())
				}
			}
		}
	}

	LaunchedEffect(response) {
		if (response != null) requestLoading.value = false
		if (response is State.Success) closeSheet()
	}
}

@Composable
private fun MediaRatingBar(
	rateValue: Int,
	rateValueChanged: Boolean,
	onRateValueChange: (Int) -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 40.dp)
			.alpha(if (rateValueChanged) 1f else 0.5f),
		horizontalArrangement = Arrangement.SpaceAround
	) {
		repeat(5) { index ->
			val starIndex = (index + 1) * 2
			val starIcon = when {
				rateValue < (starIndex - 1) -> Icons.Rounded.StarBorder
				rateValue == (starIndex - 1) -> Icons.Rounded.StarHalf
				else -> Icons.Rounded.Star
			}
			Icon(
				imageVector = starIcon,
				contentDescription = null,
				modifier = Modifier
					.size(40.dp)
					.bounceClickable {
						val newValue = when {
							!rateValueChanged && (rateValue == starIndex - 1) -> rateValue
							!rateValueChanged && (rateValue == starIndex) -> rateValue
							rateValue > starIndex -> starIndex
							rateValue == starIndex -> starIndex - 1
							rateValue == (starIndex - 1) -> starIndex
							rateValue < (starIndex - 1) -> starIndex - 1
							else -> 0
						}
						onRateValueChange(newValue)
					},
				tint = Color.Yellow
			)
		}
	}
}