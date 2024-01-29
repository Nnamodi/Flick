package com.roland.android.flick.ui.sheets

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.R
import com.roland.android.flick.models.SampleData.genreList
import com.roland.android.flick.models.SampleData.trendingMovies
import com.roland.android.flick.ui.components.ItemBackdropPoster
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Extensions.genres
import com.roland.android.flick.utils.Extensions.releaseDateRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsSheet(
	movie: Movie,
	genreList: GenreList,
	viewMore: (Int) -> Unit,
	closeSheet: () -> Unit
) {
	val sheetState = rememberModalBottomSheetState(true)
	val screenHeight = LocalConfiguration.current.screenHeightDp

	ModalBottomSheet(
		onDismissRequest = closeSheet,
		modifier = Modifier
			.absoluteOffset(y = 16.dp)
			.padding(horizontal = 12.dp),
		sheetState = sheetState,
		shape = RoundedCornerShape(28.dp),
		dragHandle = {}
	) {
		Box(
			modifier = Modifier
				.clip(BottomSheetDefaults.ExpandedShape)
				.padding(12.dp)
				.heightIn(100.dp, (screenHeight / 1.5).dp),
			contentAlignment = Alignment.BottomCenter
		) {
			Column {
				ItemBackdropPoster(
					movie = movie,
					modifier = Modifier
						.height(210.dp)
						.clip(BottomSheetDefaults.ExpandedShape)
				)
				Text(
					text = movie.title ?: movie.tvName ?: "",
					modifier = Modifier.padding(vertical = 6.dp),
					fontSize = 20.sp,
					fontWeight = FontWeight.Bold
				)
				Text(
					text = movie.genreIds.genres(genreList),
					modifier = Modifier
						.padding(bottom = 4.dp)
						.horizontalScroll(rememberScrollState()),
					color = MaterialTheme.colorScheme.surfaceTint,
					fontSize = 14.sp,
					softWrap = false
				)
				(movie.releaseDate ?: movie.firstAirDate)?.let {
					Text(
						text = stringResource(movie.releaseDateRes(), it.take(4)),
						modifier = Modifier.alpha(0.8f),
						fontSize = 12.sp,
						fontStyle = FontStyle.Italic,
						fontWeight = FontWeight.Light
					)
				}
				Text(
					text = movie.overview,
					modifier = Modifier
						.padding(top = 8.dp, bottom = ButtonDefaults.MinHeight + 20.dp)
						.verticalScroll(rememberScrollState())
				)
			}
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
		val movies = trendingMovies.collectAsLazyPagingItems().itemSnapshotList

		if (movie != null) {
			MovieDetailsSheet(
				movie = movie!!,
				genreList = genreList,
				viewMore = {}
			) {
				movie = if (movie == null) movies[0] else null
			}
		}
	}
}