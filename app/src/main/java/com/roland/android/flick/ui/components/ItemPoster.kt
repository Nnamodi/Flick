package com.roland.android.flick.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_MEDIUM
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_LARGE
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_MEDIUM
import com.roland.android.flick.utils.Constants.TMDB_POSTER_IMAGE_BASE_URL_W342
import com.roland.android.flick.utils.Constants.TMDB_POSTER_IMAGE_BASE_URL_W500
import com.roland.android.flick.utils.Extensions.roundOff

@Composable
fun LargeItemPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	onClick: (Movie) -> Unit
) {
	Poster(
		model = TMDB_POSTER_IMAGE_BASE_URL_W500 + movie.posterPath,
		contentDescription = movie.title ?: movie.tvName,
		voteAverage = movie.voteAverage,
		modifier = modifier.size(POSTER_WIDTH_LARGE, 370.dp),
		posterType = PosterType.Large
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
		modifier = modifier.size(POSTER_WIDTH_MEDIUM, POSTER_HEIGHT_MEDIUM)
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
fun Poster(
	model: String,
	contentDescription: String?,
	voteAverage: Double,
	modifier: Modifier = Modifier,
	posterType: PosterType = PosterType.Small,
	onClick: () -> Unit
) {
	Box(
		modifier = modifier
			.clip(MaterialTheme.shapes.large)
			.clickable(posterType != PosterType.BottomSheet) { onClick() }
	) {
		AsyncImage(
			model = model,
			contentDescription = contentDescription,
			modifier = Modifier.fillMaxSize(),
			contentScale = ContentScale.Crop
		)
		Row {
			Spacer(Modifier.weight(1f))
			Row(
				modifier = Modifier
					.padding(6.dp)
					.clip(MaterialTheme.shapes.large)
					.background(Color.Black.copy(alpha = 0.5f)),
				verticalAlignment = Alignment.CenterVertically
			) {
				if (posterType == PosterType.BottomSheet) {
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
						start = if (posterType == PosterType.BottomSheet) 4.dp else 10.dp,
						top = 2.dp,
						end = 10.dp,
						bottom = 2.dp
					),
					color = Color.White,
					fontSize = if (posterType == PosterType.Large) 16.sp else 14.sp
				)
			}
		}
	}
}

enum class PosterType {
	Small,
	Large,
	BottomSheet
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