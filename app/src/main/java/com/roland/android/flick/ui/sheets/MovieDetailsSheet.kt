package com.roland.android.flick.ui.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.R
import com.roland.android.flick.models.SampleData.trendingMovies
import com.roland.android.flick.ui.components.Poster
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.TMDB_POSTER_IMAGE_BASE_URL_W780

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsSheet(
	movie: Movie,
	viewMore: (Int) -> Unit,
	closeSheet: () -> Unit
) {
	val sheetState = rememberModalBottomSheetState(true)

	ModalBottomSheet(
		onDismissRequest = closeSheet,
		sheetState = sheetState,
		dragHandle = {}
	) {
		Column(
			modifier = Modifier
				.clip(BottomSheetDefaults.ExpandedShape)
				.padding(12.dp)
				.wrapContentHeight()
		) {
			Poster(
				model = TMDB_POSTER_IMAGE_BASE_URL_W780 + movie.backdropPath,
				contentDescription = null,
				voteAverage = movie.voteAverage,
				modifier = Modifier
					.fillMaxWidth()
					.height(210.dp)
					.clip(BottomSheetDefaults.ExpandedShape),
				posterType = PosterType.BottomSheet
			) {}
			Text(
				text = movie.title ?: movie.tvName ?: "",
				modifier = Modifier.padding(vertical = 8.dp),
				fontSize = 22.sp,
				fontWeight = FontWeight.Bold
			)
			(movie.releaseDate ?: movie.firstAirDate)?.let {
				val stringRes = if (movie.title != null) R.string.release_date else R.string.first_air_date

				Text(
					text = stringResource(stringRes, it.take(4)),
					fontSize = 12.sp,
					fontStyle = FontStyle.Italic,
					fontWeight = FontWeight.Light
				)
			}
			Text(
				text = movie.overview,
				modifier = Modifier.padding(vertical = 8.dp)
			)
			Button(
				onClick = { viewMore(movie.id) },
				modifier = Modifier.fillMaxWidth()
			) {
				Text(text = stringResource(R.string.more))
			}
		}
	}
}

@Preview
@Composable
fun MovieDetailsSheetPreview() {
	FlickTheme {
		var movie by remember { mutableStateOf<Movie?>(null) }

		if (movie != null) {
			MovieDetailsSheet(
				movie = movie!!,
				viewMore = {}
			) {
				movie = if (movie == null) trendingMovies.results[0] else null
			}
		}
	}
}