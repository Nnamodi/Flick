package com.roland.android.flick.ui.screens.coming_soon

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.R
import com.roland.android.flick.models.SampleData.genreList
import com.roland.android.flick.models.SampleData.movie1
import com.roland.android.flick.models.SampleData.movie5
import com.roland.android.flick.ui.components.ComingSoonItemPoster
import com.roland.android.flick.ui.components.ExpandedComingSoonPoster
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.components.PosterType.BackdropPoster
import com.roland.android.flick.ui.components.PosterType.ComingSoon
import com.roland.android.flick.ui.components.RatingBar
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.coming_soon.ComingSoonItemState.Default
import com.roland.android.flick.ui.screens.coming_soon.ComingSoonItemState.Expanded
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_X_LARGE
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_X_LARGE
import com.roland.android.flick.utils.Constants.SERIES
import com.roland.android.flick.utils.Constants.YEAR
import com.roland.android.flick.utils.Extensions.dateFormat
import com.roland.android.flick.utils.Extensions.genres
import com.roland.android.flick.utils.Extensions.releaseDateRes
import com.roland.android.flick.utils.PosterContainer
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.animatePagerItem
import com.roland.android.flick.utils.dynamicPageHeight
import com.roland.android.flick.utils.dynamicPageWidth
import com.roland.android.flick.utils.rememberWindowSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComingSoonItem(
	movie: Movie,
	genreList: GenreList,
	expanded: Boolean,
	itemPage: Int,
	pagerState: PagerState,
	maxHeight: Dp,
	maxWidth: Dp,
	onExpand: () -> Unit,
	viewMore: (Screens) -> Unit,
	minimize: () -> Unit
) {
	val windowSize = rememberWindowSize()
	val inPortraitMode by remember(windowSize.width) {
		derivedStateOf { windowSize.width == WindowType.Portrait }
	}
	val durationMillis = 1000
	val defaultDurationMillis = 700
	val itemTransition = updateTransition(
		targetState = if (expanded) Expanded else Default,
		label = "item transition"
	)
	val cornerSize by itemTransition.animateDp(
		transitionSpec = { tween(
			if (Default isTransitioningTo Expanded) durationMillis else defaultDurationMillis
		) },
		label = "corner size",
		targetValueByState = { state ->
			if (state == Expanded) 0.dp else 12.dp
		}
	)
	val itemPadding by itemTransition.animateDp(
		transitionSpec = { tween(
			if (Default isTransitioningTo Expanded) durationMillis else defaultDurationMillis
		) },
		label = "item padding",
		targetValueByState = { state ->
			if (state == Expanded) PADDING_WIDTH else 0.dp
		}
	)
	val posterHeight by itemTransition.animateDp(
		transitionSpec = { tween(
			if (Default isTransitioningTo Expanded) durationMillis else defaultDurationMillis
		) },
		label = "poster height",
		targetValueByState = { state ->
			when {
				state == Expanded && inPortraitMode -> maxHeight * 0.43f
				state == Expanded && !inPortraitMode -> maxHeight
				state == Default && !inPortraitMode -> dynamicPageHeight(POSTER_HEIGHT_X_LARGE)
				else -> POSTER_HEIGHT_X_LARGE
			}
		}
	)
	val posterWidth by itemTransition.animateDp(
		transitionSpec = { tween(
			if (Default isTransitioningTo Expanded) durationMillis else defaultDurationMillis
		) },
		label = "poster width",
		targetValueByState = { state ->
			when {
				state == Expanded && inPortraitMode -> maxWidth
				state == Expanded && !inPortraitMode -> maxWidth / 2
				state == Default && !inPortraitMode -> dynamicPageWidth(POSTER_WIDTH_X_LARGE)
				else -> POSTER_WIDTH_X_LARGE
			}
		}
	)
	val itemHeight by itemTransition.animateDp(
		transitionSpec = { tween(
			if (Default isTransitioningTo Expanded) durationMillis else defaultDurationMillis
		) },
		label = "item height",
		targetValueByState = { state ->
			when {
				state == Expanded -> maxHeight
				state == Default && !inPortraitMode -> dynamicPageHeight(POSTER_HEIGHT_X_LARGE)
				else -> POSTER_HEIGHT_X_LARGE
			}
		}
	)
	val itemWidth by itemTransition.animateDp(
		transitionSpec = { tween(
			if (Default isTransitioningTo Expanded) durationMillis else defaultDurationMillis
		) },
		label = "item width",
		targetValueByState = { state ->
			when {
				state == Expanded -> maxWidth
				state == Default && !inPortraitMode -> dynamicPageWidth(POSTER_WIDTH_X_LARGE)
				else -> POSTER_WIDTH_X_LARGE
			}
		}
	)

	Box(
		modifier = Modifier
			.size(itemWidth, itemHeight)
			.clip(RoundedCornerShape(cornerSize))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				enabled = expanded
			) { if (!itemTransition.isRunning) minimize() }
			.padding(start = itemPadding, end = itemPadding, bottom = itemPadding)
	) {
		itemTransition.AnimatedContent(
			transitionSpec = {
				when {
					Expanded isTransitioningTo Default -> {
						fadeIn(tween(durationMillis = defaultDurationMillis, delayMillis = 70, easing = LinearEasing)) togetherWith
								fadeOut(tween(durationMillis = 200, delayMillis = 30, easing = LinearEasing))
					}
					Default isTransitioningTo Expanded -> {
						fadeIn(tween(durationMillis = durationMillis, delayMillis = 30, easing = LinearEasing)) togetherWith
								fadeOut(tween(durationMillis = 200, delayMillis = 50, easing = LinearEasing))
					}
					else -> EnterTransition.None togetherWith ExitTransition.None
				}.using(SizeTransform(false))
			}
		) { targetState ->
			PosterContainer {
				ComingSoonItemPoster(
					movie = movie,
					modifier = Modifier
						.animatePagerItem(itemPage, pagerState)
						.size(posterWidth, posterHeight)
						.padding(end = if (!inPortraitMode) itemPadding else 0.dp),
					posterType = if (targetState == Expanded) BackdropPoster else ComingSoon,
					posterFromPager = targetState == Default,
					onClick = { if (!itemTransition.isRunning) onExpand() }
				)
				if (targetState == Expanded) {
					ItemDetails(
						movie = movie,
						genreList = genreList,
						viewMore = viewMore
					)
				}
			}
		}
	}
}

@Composable
fun ItemDetails(
	movie: Movie,
	genreList: GenreList,
	inBottomSheet: Boolean = false,
	viewMore: (Screens) -> Unit
) {
	Column {
		Text(
			text = movie.title ?: movie.tvName ?: "",
			modifier = Modifier.padding(vertical = if (inBottomSheet) 6.dp else 10.dp),
			fontSize = if (inBottomSheet) 20.sp else 22.sp,
			fontWeight = FontWeight.Bold
		)
		if (inBottomSheet) {
			Text(
				text = movie.genreIds.genres(genreList),
				modifier = Modifier
					.padding(bottom = 4.dp)
					.horizontalScroll(rememberScrollState()),
				color = MaterialTheme.colorScheme.surfaceTint,
				fontSize = 14.sp,
				softWrap = false
			)
		} else {
			GenreRowItems(movie.genreIds.genres(genreList))
		}
		(movie.releaseDate ?: movie.firstAirDate)?.let { date ->
			Text(
				text = stringResource(movie.releaseDateRes(), date.dateFormat(YEAR)),
				modifier = Modifier.alpha(0.8f),
				fontSize = 12.sp,
				fontStyle = FontStyle.Italic,
				fontWeight = FontWeight.Light
			)
		}
		Text(
			text = movie.overview,
			modifier = Modifier
				.weight(1f)
				.padding(
					top = if (inBottomSheet) 8.dp else 14.dp,
					bottom = 12.dp
				)
				.verticalScroll(rememberScrollState())
		)
		Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
			if (!inBottomSheet) {
				(movie.releaseDate ?: movie.firstAirDate)?.let { date ->
					Text(
						text = stringResource(R.string.date_to_release, date.dateFormat()),
						modifier = Modifier.padding(bottom = 12.dp),
						color = MaterialTheme.colorScheme.surfaceTint
					)
				}
			}
			val movieType = if (movie.title != null) MOVIES else SERIES
			val navInfo = Screens.MovieDetailsScreen(movieType, movie.id.toString())
			Button(
				onClick = { viewMore(navInfo) },
				modifier = if (inBottomSheet) Modifier.fillMaxWidth() else Modifier
			) {
				Text(
					text = stringResource(R.string.more),
					modifier = Modifier.padding(horizontal = if (inBottomSheet) 0.dp else 20.dp)
				)
			}
		}
	}
}

@Composable
private fun FullScreenComingSoonItem(
	movie: Movie,
	genreList: GenreList,
	viewMore: (Int) -> Unit,
	minimize: () -> Unit
) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null
			) { minimize() },
		contentAlignment = Alignment.BottomCenter
	) {
		Box(Modifier.fillMaxSize()) {
			IconButton(
				onClick = minimize,
				modifier = Modifier
					.statusBarsPadding()
					.padding(12.dp)
			) {
				Icon(Icons.Rounded.ArrowBackIos, stringResource(R.string.back))
			}
		}
		ExpandedComingSoonPoster(
			movie = movie,
			modifier = Modifier.fillMaxSize(),
			posterType = PosterType.FullScreen
		)
		Box(
			modifier = Modifier
				.fillMaxHeight(0.45f)
				.padding(PADDING_WIDTH)
				.clip(MaterialTheme.shapes.extraLarge)
				.background(Color.Gray.copy(alpha = 0.95f))
		) {
			RatingBar(
				posterType = PosterType.FullScreen,
				voteAverage = movie.voteAverage
			)
			Column(Modifier.padding(PADDING_WIDTH + 10.dp)) {
				Text(
					text = movie.title ?: movie.tvName ?: "",
					modifier = Modifier.padding(vertical = 10.dp),
					color = Color.White,
					fontSize = 22.sp,
					fontWeight = FontWeight.Bold
				)
				GenreRowItems(movie.genreIds.genres(genreList))
				(movie.releaseDate ?: movie.firstAirDate)?.let {
					Text(
						text = stringResource(movie.releaseDateRes(), it.take(4)),
						modifier = Modifier.alpha(0.8f),
						color = Color.White,
						fontSize = 12.sp,
						fontStyle = FontStyle.Italic,
						fontWeight = FontWeight.Light
					)
				}
				Text(
					text = movie.overview,
					modifier = Modifier
						.padding(top = 14.dp, bottom = 20.dp)
						.weight(1f)
						.verticalScroll(rememberScrollState()),
					color = Color.White
				)
				Row(Modifier.fillMaxWidth(), Arrangement.Center) {
					Button(onClick = { viewMore(movie.id) }) {
						Text(
							text = stringResource(R.string.more),
							modifier = Modifier.padding(horizontal = 20.dp)
						)
					}
				}
			}
		}
	}
}

@Composable
private fun GenreRowItems(genres: String) {
	val genreList = genres.split(", ")

	Row(
		modifier = Modifier
			.horizontalScroll(rememberScrollState())
			.padding(bottom = 8.dp)
	) {
		genreList.forEach { genre ->
			Box(
				modifier = Modifier
					.padding(end = 10.dp)
					.clip(MaterialTheme.shapes.extraLarge)
					.border(
						width = 1.dp,
						color = MaterialTheme.colorScheme.surfaceTint,
						shape = MaterialTheme.shapes.extraLarge
					)
			) {
				Text(
					text = genre,
					modifier = Modifier.padding(8.dp , 6.dp),
					color = MaterialTheme.colorScheme.surfaceTint,
					fontSize = 14.sp,
				)
			}
		}
	}
}

private enum class ComingSoonItemState {
	Default, Expanded
}

@Preview(showBackground = true)
@Composable
private fun FullScreenComingSoonItemPreview() {
	FlickTheme {
		FullScreenComingSoonItem(
			movie = movie1,
			genreList = genreList,
			viewMore = {},
			minimize = {}
		)
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun ComingSoonItemPreview() {
	FlickTheme(true) {
		Surface {
			val expanded = rememberSaveable { mutableStateOf(false) }

			BoxWithConstraints(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				ComingSoonItem(
					movie = movie5,
					genreList = genreList,
					expanded = !expanded.value,
					itemPage = 0,
					pagerState = rememberPagerState { 1 },
					maxHeight = maxHeight,
					maxWidth = maxWidth,
					onExpand = { expanded.value = true },
					viewMore = {}
				) { expanded.value = false }
			}
		}
	}
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun ComingSoonItemLandscapePreview() {
	ComingSoonItemPreview()
}