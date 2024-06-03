package com.roland.android.flick.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Empty
import coil.request.ImageRequest
import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.R
import com.roland.android.flick.ui.components.PosterType.BackdropPoster
import com.roland.android.flick.ui.components.PosterType.FullScreen
import com.roland.android.flick.ui.components.PosterType.Large
import com.roland.android.flick.ui.components.PosterType.Small
import com.roland.android.flick.utils.Constants.CAST_IMAGE_BASE_URL_W185
import com.roland.android.flick.utils.Constants.CAST_IMAGE_BASE_URL_W342
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.MOVIE_IMAGE_BASE_URL_W342
import com.roland.android.flick.utils.Constants.MOVIE_IMAGE_BASE_URL_W780
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_LARGE
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_MEDIUM
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_SMALL
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_LARGE
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_MEDIUM
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_SMALL
import com.roland.android.flick.utils.Constants.SERIES
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.animatePagerItem
import com.roland.android.flick.utils.bounceClickable
import com.roland.android.flick.utils.dynamicPageSize
import com.roland.android.flick.utils.getPoster
import com.roland.android.flick.utils.painterPlaceholder
import com.roland.android.flick.utils.rememberWindowSize

typealias MediaId = Int
typealias MediaType = String

@Composable
fun ExpandedComingSoonPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	posterType: PosterType = BackdropPoster
) {
	Poster(
		model = MOVIE_IMAGE_BASE_URL_W780 + movie.backdropPath,
		contentDescription = null,
		voteAverage = movie.voteAverage,
		modifier = modifier.fillMaxWidth(),
		posterType = posterType
	) {}
}

@Composable
fun ItemBackdropPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	posterType: PosterType = BackdropPoster
) {
	Poster(
		model = MOVIE_IMAGE_BASE_URL_W780 + movie.backdropPath,
		contentDescription = null,
		voteAverage = movie.voteAverage,
		modifier = modifier.fillMaxWidth(),
		posterType = posterType
	) {}
}

@Composable
fun VideoThumbnail(
	thumbnail: String,
	trailerAvailable: Boolean,
	modifier: Modifier = Modifier
) {
	Box(modifier, Alignment.Center) {
		AsyncImage(
			model = MOVIE_IMAGE_BASE_URL_W780 + thumbnail,
			contentDescription = stringResource(R.string.trailer_loading),
			modifier = Modifier
				.fillMaxSize()
				.drawBehind { drawRect(Color.Black) },
			contentScale = ContentScale.Crop
		)
		if (trailerAvailable) {
			CircularProgressIndicator(
				modifier = Modifier
					.clip(CircleShape)
					.background(Color.Black.copy(alpha = 0.5f))
					.padding(4.dp)
			)
		} else {
			val clicked = rememberSaveable { mutableStateOf(false) }
			Box(
				Modifier
					.bounceClickable { clicked.value = !clicked.value }
					.clip(CircleShape)
					.background(Color.Black.copy(alpha = 0.8f))
					.padding(4.dp)
					.animateContentSize()
			) {
				if (clicked.value) {
					Text(stringResource(R.string.no_trailer_available), Modifier.padding(8.dp), Color.White)
				} else {
					Icon(Icons.Rounded.Error, null, Modifier.size(40.dp), Color.White)
				}
			}
		}
	}
}

@Composable
fun ComingSoonItemPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	posterType: PosterType = BackdropPoster,
	posterFromPager: Boolean,
	clickable: Boolean,
	onClick: () -> Unit
) {
	val isBackdrop = posterType == BackdropPoster
	val windowSize = rememberWindowSize()
	val inPortraitMode = remember(windowSize.width) {
		derivedStateOf { windowSize.width == WindowType.Portrait }
	}

	Poster(
		model = (if (isBackdrop || !inPortraitMode.value) {
			movie.backdropPath
		} else movie.posterPath).getPoster(isBackdrop),
		contentDescription = movie.title ?: movie.tvName,
		voteAverage = movie.voteAverage,
		modifier = modifier,
		posterType = posterType,
		posterFromPager = posterFromPager,
		showVoteAverage = false
	) { if (clickable) onClick() }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LargeItemPoster(
	movie: Movie,
	itemPage: Int,
	pagerState: PagerState,
	onClick: (Movie) -> Unit
) {
	val windowSize = rememberWindowSize()
	val inPortraitMode = remember(windowSize.width) {
		derivedStateOf { windowSize.width == WindowType.Portrait }
	}

	Poster(
		model = (if (inPortraitMode.value) {
			movie.posterPath
		} else movie.backdropPath).getPoster(),
		contentDescription = movie.title ?: movie.tvName,
		voteAverage = movie.voteAverage,
		modifier = Modifier
			.dynamicPageSize(POSTER_WIDTH_LARGE, POSTER_HEIGHT_LARGE)
			.animatePagerItem(itemPage, pagerState),
		posterType = Large,
		posterFromPager = true,
	) { onClick(movie) }
}

@Composable
fun MediumItemPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	isCancellable: Boolean,
	requestDone: Boolean,
	onClick: (Movie) -> Unit,
	onCancel: (MediaId, MediaType) -> Unit
) {
	if (isCancellable) {
		CancellableItemPoster(
			movie = movie,
			requestDone = requestDone,
			posterType = PosterType.Medium,
			onClick = onClick,
			onCancel = onCancel
		)
	} else {
		Poster(
			model = MOVIE_IMAGE_BASE_URL_W342 + movie.posterPath,
			contentDescription = movie.title ?: movie.tvName,
			voteAverage = movie.voteAverage,
			modifier = modifier
				.padding(6.dp)
				.size(POSTER_WIDTH_MEDIUM, POSTER_HEIGHT_MEDIUM),
			posterType = PosterType.Medium
		) { onClick(movie) }
	}
}

@Composable
fun SmallItemPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	onClick: (Movie) -> Unit
) {
	Poster(
		model = MOVIE_IMAGE_BASE_URL_W342 + movie.posterPath,
		contentDescription = movie.title ?: movie.tvName,
		voteAverage = movie.voteAverage,
		modifier = modifier
			.padding(end = 12.dp)
			.size(POSTER_WIDTH_SMALL, POSTER_HEIGHT_SMALL)
	) { onClick(movie) }
}

@Composable
fun CancellableItemPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	requestDone: Boolean,
	showUserRating: Boolean = false,
	posterType: PosterType = Small,
	onClick: (Movie) -> Unit,
	onCancel: (MediaId, MediaType) -> Unit
) {
	val requestLoading = rememberSaveable { mutableStateOf(false) }
	val actionHandled = rememberSaveable(requestDone) { mutableStateOf(requestDone) }
	val paddingModifier = if (posterType == Small) modifier.padding(end = 12.dp) else modifier.padding(6.dp)

	Column(
		modifier = paddingModifier
			.width(if (posterType == Small) POSTER_WIDTH_SMALL else POSTER_WIDTH_MEDIUM)
			.bounceClickable { onClick(movie) }
			.clip(MaterialTheme.shapes.large)
			.background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Box(
			modifier = Modifier
				.height(if (posterType == Small) POSTER_HEIGHT_SMALL else POSTER_HEIGHT_MEDIUM)
				.clip(MaterialTheme.shapes.large)
		) {
			val mediaType = if (movie.title == null) SERIES else MOVIES
			val state = remember { mutableStateOf<AsyncImagePainter.State>(Empty) }

			AsyncImage(
				model = MOVIE_IMAGE_BASE_URL_W342 + movie.posterPath,
				contentDescription = movie.title ?: movie.tvName,
				modifier = Modifier
					.fillMaxSize()
					.painterPlaceholder(state.value),
				onState = { state.value = it },
				contentScale = ContentScale.Crop
			)
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically
			) {
				if (requestLoading.value) {
					CircularProgressIndicator(
						modifier = Modifier
							.padding(6.dp)
							.size(20.dp),
						strokeWidth = 2.dp
					)
				} else {
					Icon(
						imageVector = Icons.Rounded.Cancel,
						contentDescription = null,
						modifier = Modifier
							.padding(6.dp)
							.scale(0.8f)
							.clickable {
								onCancel(movie.id, mediaType)
								requestLoading.value = true
							}
							.clip(CircleShape)
							.background(Color.Black),
						tint = Color.White
					)
				}
				Spacer(Modifier.weight(1f))
				RatingBar(Small, movie.voteAverage)
			}
		}

		if (showUserRating) {
			Text(
				text = stringResource(R.string.your_rating, movie.rating),
				modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
				fontSize = 12.sp,
				fontStyle = FontStyle.Italic,
				textAlign = TextAlign.Center
			)
		}
	}

	LaunchedEffect(actionHandled) {
		if (!actionHandled.value) return@LaunchedEffect
		requestLoading.value = false
		actionHandled.value = false
	}
}

@Composable
private fun Poster(
	model: String,
	contentDescription: String?,
	voteAverage: Double,
	modifier: Modifier = Modifier,
	posterType: PosterType = Small,
	posterFromPager: Boolean = false,
	showVoteAverage: Boolean = true,
	onClick: () -> Unit
) {
	val state = remember { mutableStateOf<AsyncImagePainter.State>(Empty) }
	val posterIsVeryLarge = posterType == BackdropPoster || posterType == FullScreen
	val windowSize = rememberWindowSize()
	val inPortraitMode = remember(windowSize.width) {
		derivedStateOf { windowSize.width == WindowType.Portrait }
	}

	Box(
		modifier = modifier
			.bounceClickable(!posterIsVeryLarge) { onClick() }
			.clip(MaterialTheme.shapes.large)
	) {
		AsyncImage(
			model = ImageRequest.Builder(LocalContext.current)
				.data(model)
				.crossfade(true)
				.build(),
			contentDescription = contentDescription,
			modifier = Modifier
				.fillMaxSize()
				.painterPlaceholder(state.value),
			onState = { state.value = it },
			contentScale = ContentScale.Crop
		)
		if (posterType != FullScreen && showVoteAverage) {
			RatingBar(posterType, voteAverage)
		}
		if (!inPortraitMode.value && posterFromPager) {
			contentDescription?.let {
				Text(
					text = it,
					modifier = Modifier
						.align(Alignment.BottomStart)
						.clip(RoundedCornerShape(topEnd = 12.dp))
						.background(Color.Black.copy(alpha = 0.65f))
						.padding(10.dp),
					color = Color.White,
					fontSize = 22.sp,
					fontWeight = FontWeight.Bold
				)
			}
		}
	}
}

@Composable
fun CastPoster(
	cast: Cast,
	modifier: Modifier = Modifier,
	posterType: PosterType = Small
) {
	val state = remember { mutableStateOf<AsyncImagePainter.State>(Empty) }
	val imageModifier = if (posterType == Small) {
		modifier
			.size(100.dp)
			.clip(CircleShape)
			.border(
				width = 2.dp,
				color = MaterialTheme.colorScheme.surfaceTint,
				shape = CircleShape
			)
	} else {
		modifier.size(140.dp, 190.dp)
	}
	val baseUrl = if (posterType == Small) CAST_IMAGE_BASE_URL_W185 else CAST_IMAGE_BASE_URL_W342

	Box(modifier = imageModifier) {
		AsyncImage(
			model = ImageRequest.Builder(LocalContext.current)
				.data(baseUrl + cast.profilePath)
				.crossfade(true)
				.build(),
			contentDescription = cast.name,
			modifier = Modifier
				.fillMaxSize()
				.painterPlaceholder(state.value),
			onState = { state.value = it },
			contentScale = ContentScale.Crop
		)
	}
}

enum class PosterType {
	Small,
	Medium,
	Large,
	ComingSoon,
	BackdropPoster,
	FullScreen
}