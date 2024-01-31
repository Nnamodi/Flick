package com.roland.android.flick.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Empty
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.ui.components.PosterType.BackdropPoster
import com.roland.android.flick.ui.components.PosterType.FullScreen
import com.roland.android.flick.ui.components.PosterType.Large
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_LARGE
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_MEDIUM
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_LARGE
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_MEDIUM
import com.roland.android.flick.utils.Constants.TMDB_POSTER_IMAGE_BASE_URL_W342
import com.roland.android.flick.utils.Constants.TMDB_POSTER_IMAGE_BASE_URL_W780
import com.roland.android.flick.utils.Extensions.roundOff
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.bounceClickable
import com.roland.android.flick.utils.dynamicPageSize
import com.roland.android.flick.utils.getPoster
import com.roland.android.flick.utils.painterPlaceholder
import com.roland.android.flick.utils.rememberWindowSize

@Composable
fun ExpandedComingSoonPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	posterType: PosterType = BackdropPoster
) {
	Poster(
		model = TMDB_POSTER_IMAGE_BASE_URL_W780 + movie.backdropPath,
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
		model = TMDB_POSTER_IMAGE_BASE_URL_W780 + movie.backdropPath,
		contentDescription = null,
		voteAverage = movie.voteAverage,
		modifier = modifier.fillMaxWidth(),
		posterType = posterType
	) {}
}

@Composable
fun ComingSoonItemPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	posterType: PosterType = BackdropPoster,
	posterFromPager: Boolean,
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
		posterFromPager = posterFromPager
	) { onClick() }
}

@Composable
fun LargeItemPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
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
		modifier = modifier.dynamicPageSize(POSTER_WIDTH_LARGE, POSTER_HEIGHT_LARGE),
		posterType = Large,
		posterFromPager = true,
	) { onClick(movie) }
}

@Composable
fun MediumItemPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	onClick: (Movie) -> Unit
) {
	Poster(
		model = TMDB_POSTER_IMAGE_BASE_URL_W342 + movie.posterPath,
		contentDescription = movie.title ?: movie.tvName,
		voteAverage = movie.voteAverage,
		modifier = modifier.size(POSTER_WIDTH_MEDIUM, POSTER_HEIGHT_MEDIUM),
		posterType = PosterType.Medium
	) { onClick(movie) }
}

@Composable
fun SmallItemPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	onClick: (Movie) -> Unit
) {
	Poster(
		model = TMDB_POSTER_IMAGE_BASE_URL_W342 + movie.posterPath,
		contentDescription = movie.title ?: movie.tvName,
		voteAverage = movie.voteAverage,
		modifier = modifier.size(POSTER_WIDTH_MEDIUM, 180.dp)
	) { onClick(movie) }
}

@Composable
private fun Poster(
	model: String,
	contentDescription: String?,
	voteAverage: Double,
	modifier: Modifier = Modifier,
	posterType: PosterType = PosterType.Small,
	posterFromPager: Boolean = false,
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
			model = model,
			contentDescription = contentDescription,
			modifier = Modifier
				.fillMaxSize()
				.painterPlaceholder(state.value),
			onState = { state.value = it },
			contentScale = ContentScale.Crop
		)
		if (posterType != FullScreen) {
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
					fontSize = 22.sp,
					fontWeight = FontWeight.Bold
				)
			}
		}
	}
}

@Composable
fun RatingBar(
	posterType: PosterType,
	voteAverage: Double,
) {
	Row {
		Spacer(Modifier.weight(1f))
		Row(
			modifier = Modifier
				.padding(6.dp)
				.clip(MaterialTheme.shapes.large)
				.background(Color.Black.copy(alpha = 0.5f)),
			verticalAlignment = Alignment.CenterVertically
		) {
			val posterIsVeryLarge = posterType == BackdropPoster || posterType == FullScreen
			if (posterIsVeryLarge) {
				Icon(
					imageVector = Icons.Rounded.StarRate,
					contentDescription = null,
					modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 2.dp),
					tint = MaterialTheme.colorScheme.surfaceTint
				)
			}
			Text(
				text = voteAverage.roundOff(),
				modifier = Modifier.padding(
					start = if (posterIsVeryLarge) 4.dp else 10.dp,
					top = 2.dp,
					end = 10.dp,
					bottom = 2.dp
				),
				color = Color.White,
				fontSize = if (posterType == Large) 16.sp else 14.sp
			)
		}
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

@Composable
fun Header(
	header: String,
	modifier: Modifier = Modifier
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		Divider(
			modifier = Modifier
				.padding(vertical = 8.dp)
				.size(4.dp, 18.dp)
				.clip(MaterialTheme.shapes.medium),
			color = MaterialTheme.colorScheme.surfaceTint
		)
		Text(
			text = header,
			modifier = Modifier.padding(start = 4.dp),
			fontWeight = FontWeight.Bold,
			fontSize = 16.sp
		)
	}
}