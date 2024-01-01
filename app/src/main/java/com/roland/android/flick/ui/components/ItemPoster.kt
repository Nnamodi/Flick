package com.roland.android.flick.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieList
import com.roland.android.flick.R
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_LARGE
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_MEDIUM
import com.roland.android.flick.utils.Constants.TMDB_POSTER_IMAGE_BASE_URL_W342
import com.roland.android.flick.utils.Constants.TMDB_POSTER_IMAGE_BASE_URL_W500

@Composable
fun HorizontalPosters(
	movieList: MovieList,
	header: String,
	seeAll: () -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(bottom = 12.dp)) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = header,
				modifier = Modifier.padding(PADDING_WIDTH, 16.dp)
			)
			Spacer(Modifier.weight(1f))
			Text(
				text = stringResource(R.string.see_all),
				modifier = Modifier
					.clip(MaterialTheme.shapes.small)
					.padding(PADDING_WIDTH, 10.dp)
					.clickable { seeAll() }
					.padding(6.dp),
				color = Color.Gray
			)
		}
		LazyRow(
			contentPadding = PaddingValues(horizontal = PADDING_WIDTH)
		) {
			itemsIndexed(
				items = movieList.results.take(15),
				key = { _, movie -> movie.id}
			) { _, movie ->
				MediumItemPoster(
					movie = movie,
					onClick = {}
				)
			}
		}
	}
}

@Composable
fun LargeItemPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	onClick: (Int) -> Unit
) {
	Poster(
		model = TMDB_POSTER_IMAGE_BASE_URL_W500 + movie.posterPath,
		contentDescription = movie.title ?: movie.tvName,
		modifier = modifier.size(POSTER_WIDTH_LARGE, 370.dp),
		onClick = { onClick(movie.id) },
	)
}

@Composable
fun MediumItemPoster(
	movie: Movie,
	modifier: Modifier = Modifier,
	onClick: (Int) -> Unit
) {
	Poster(
		model = TMDB_POSTER_IMAGE_BASE_URL_W342 + movie.posterPath,
		contentDescription = movie.title ?: movie.tvName,
		modifier = modifier
			.size(POSTER_WIDTH_MEDIUM, 180.dp)
			.padding(end = 12.dp),
		onClick = { onClick(movie.id) },
	)
}

@Composable
private fun Poster(
	model: String,
	contentDescription: String?,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	AsyncImage(
		model = model,
		contentDescription = contentDescription,
		modifier = modifier
			.clip(MaterialTheme.shapes.large)
			.clickable { onClick() },
		contentScale = ContentScale.Crop
	)
}