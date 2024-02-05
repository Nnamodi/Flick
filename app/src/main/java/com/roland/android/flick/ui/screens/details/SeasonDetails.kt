package com.roland.android.flick.ui.screens.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Empty
import com.roland.android.domain.entity.Episode
import com.roland.android.flick.R
import com.roland.android.flick.models.SeasonDetailsModel
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.Header
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.components.RatingBar
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.utils.Constants
import com.roland.android.flick.utils.painterPlaceholder

@Composable
fun SeasonDetails(
	seasonUiState: State<SeasonDetailsModel>?,
	openSeasonSelectionSheet: () -> Unit
) {
	CommonScreen(state = seasonUiState, loadingScreen = {}) { data ->
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 12.dp)
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(Constants.PADDING_WIDTH),
				verticalAlignment = Alignment.CenterVertically
			) {
				Header(header = stringResource(R.string.episodes))
				Spacer(Modifier.weight(1f))
				AssistChip(
					onClick = openSeasonSelectionSheet,
					label = {
						Text(stringResource(R.string.season_number, data.season.seasonNumber))
					},
					modifier = Modifier.padding(6.dp),
					trailingIcon = {
						if (data.season.seasonNumber > 1) {
							Icon(Icons.Rounded.ArrowDropDown, stringResource(R.string.more_seasons))
						}
					},
					colors = AssistChipDefaults.assistChipColors(
						containerColor = MaterialTheme.colorScheme.primaryContainer,
						labelColor = MaterialTheme.colorScheme.onPrimaryContainer
					)
				)
			}
			LazyRow(
				contentPadding = PaddingValues(
					start = Constants.PADDING_WIDTH,
					end = Constants.PADDING_WIDTH - 12.dp
				)
			) {
				data.season.episodes?.let { episodeList ->
					items(episodeList.size) { index ->
						EpisodePoster(
							episode = episodeList[index],
							modifier = Modifier.padding(end = 12.dp)
						)
					}
				}
				item {
//					episodeList.loadStateUi(PosterType.Small)
				}
			}
		}
	}
}

@Composable
private fun EpisodePoster(
	episode: Episode,
	modifier: Modifier = Modifier
) {
	val state = remember { mutableStateOf<AsyncImagePainter.State>(Empty) }

	Column {
		Box(
			modifier = modifier
				.clip(MaterialTheme.shapes.large)
				.size(180.dp, 130.dp)
		) {
			AsyncImage(
				model = episode.stillPath,
				contentDescription = episode.name,
				modifier = Modifier
					.fillMaxSize()
					.painterPlaceholder(state.value),
				onState = { state.value = it },
				contentScale = ContentScale.Crop
			)
			RatingBar(PosterType.Small, episode.voteAverage)
		}
		Text(episode.name, Modifier.padding(vertical = 8.dp))
	}
}