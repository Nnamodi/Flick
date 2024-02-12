package com.roland.android.flick.ui.screens.details.sheets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.CastDetails
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.R
import com.roland.android.flick.models.CastDetailsModel
import com.roland.android.flick.models.SampleData.movieCastDetails
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.CastPoster
import com.roland.android.flick.ui.components.HorizontalPosters
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.screens.details.loading.CastDetailsLoadingUi
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.DynamicContainer
import com.roland.android.flick.utils.Extensions.refactor
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CastDetailsSheet(
	uiState: State<CastDetailsModel>?,
	onMovieClick: (Movie) -> Unit,
	closeSheet: () -> Unit
) {
	val screenHeight = LocalConfiguration.current.screenHeightDp
	val windowSize = rememberWindowSize()
	val inPortraitMode by remember(windowSize.width) {
		derivedStateOf { windowSize.width == WindowType.Portrait }
	}

	ModalBottomSheet(
		onDismissRequest = closeSheet,
		modifier = Modifier.padding(horizontal = 12.dp),
		sheetState = rememberModalBottomSheetState(true),
		shape = RoundedCornerShape(28.dp),
		dragHandle = null
	) {
		CommonScreen(
			state = uiState,
			loadingScreen = { error ->
				CastDetailsLoadingUi(isLoading = error == null)
			}
		) {	data ->
			val castDetails = data.castDetails

			DynamicContainer(Modifier.heightIn(100.dp, (screenHeight - 80).dp)) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Card(
						modifier = Modifier
							.padding(horizontal = PADDING_WIDTH)
							.padding(top = PADDING_WIDTH)
							.size(140.dp, 190.dp),
						shape = MaterialTheme.shapes.large,
						elevation = CardDefaults.cardElevation(10.dp)
					) {
						CastPoster(
							cast = Cast(
								name = castDetails.name,
								profilePath = castDetails.profilePath
							),
							posterType = PosterType.Medium
						)
					}
					Details(castDetails)
				}
				if (!inPortraitMode) {
					Divider(Modifier
						.padding(vertical = PADDING_WIDTH)
						.fillMaxHeight()
						.width(2.dp)
					)
				}
				castDetails.biography?.let { biography ->
					var expandBiography by remember { mutableStateOf(false) }
					val modifier = when {
						expandBiography && biography.length <= 150 -> Modifier.wrapContentHeight()
						expandBiography -> Modifier.weight(1f)
						else -> Modifier
					}.animateContentSize(tween(300))

					if (biography.isNotEmpty() && inPortraitMode) {
						CastBiography(
							biography = biography,
							expandBiography = expandBiography,
							modifier = modifier,
							onExpandBiography = { expandBiography = it }
						)
					}
				}
				HorizontalPosters(
					movieList = castDetails.moviesAndShowsActed.refactor(),
					header = stringResource(R.string.filmography),
					onMovieClick = onMovieClick
				)
			}
		}
	}
}

@Composable
private fun Details(castDetails: CastDetails) {
	Column(Modifier.padding(end = PADDING_WIDTH)) {
		Text(
			text = castDetails.name,
			modifier = Modifier.padding(top = 10.dp, bottom = 4.dp),
			fontSize = 18.sp,
			fontWeight = FontWeight.Bold
		)
		Row(Modifier.padding(top = 6.dp, bottom = 10.dp)) {
			Text(
				text = stringResource(R.string.known_for),
				modifier = Modifier.padding(end = 4.dp),
				fontSize = 14.sp,
				fontStyle = FontStyle.Italic
			)
			Text(
				text = castDetails.knownForDepartment,
				fontSize = 14.sp,
				maxLines = 2
			)
		}
	}
}

@Composable
private fun CastBiography(
	biography: String,
	expandBiography: Boolean,
	modifier: Modifier,
	onExpandBiography: (Boolean) -> Unit
) {
	Column(modifier) {
		Text(
			text = stringResource(R.string.biography),
			modifier = Modifier.padding(start = PADDING_WIDTH, top = 14.dp),
			fontSize = 18.sp,
			fontStyle = FontStyle.Italic,
			fontWeight = FontWeight.Bold
		)
		Text(
			text = biography,
			modifier = Modifier
				.padding(start = 12.dp, top = 12.dp, end = 12.dp)
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null
				) { onExpandBiography(!expandBiography) }
				.verticalScroll(rememberScrollState()),
			overflow = TextOverflow.Ellipsis,
			maxLines = if (expandBiography) Int.MAX_VALUE else 2
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun CastDetailsSheetPreview() {
	FlickTheme(true) {
		val castDetails = State.Success(CastDetailsModel(movieCastDetails))
		var uiState by remember { mutableStateOf<State.Success<CastDetailsModel>?>(castDetails) }

		Surface(
			Modifier
				.fillMaxSize()
				.clickable { uiState = castDetails }
		) {
			if (uiState != null) {
				CastDetailsSheet(
					uiState = uiState,
					onMovieClick = {}
				) { uiState = null }
			}
		}
	}
}