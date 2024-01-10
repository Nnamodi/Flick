package com.roland.android.flick.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.state.MovieListUiState
import com.roland.android.flick.ui.components.MediumItemPoster
import com.roland.android.flick.ui.components.MovieListTopBar
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.utils.Extensions.getName

@Composable
fun MovieListScreen(
	uiState: MovieListUiState,
	category: String,
	navigateUp: () -> Unit
) {
	val (movieList) = uiState
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }

	Scaffold(
		topBar = {
			MovieListTopBar(
				title = stringResource(category.getName()),
				navigateUp = navigateUp
			)
		}
	) { paddingValues ->
		CommonScreen(movieList) { data ->
			LazyVerticalGrid(
				columns = GridCells.Adaptive(100.dp),
				modifier = Modifier
					.padding(paddingValues)
					.padding(horizontal = 6.dp),
				contentPadding = PaddingValues(bottom = 50.dp)
			) {
				itemsIndexed(
					items = data.movieList.results,
					key = { _, movie -> movie.id }
				) { _, movie ->
					MediumItemPoster(
						movie = movie,
						modifier = Modifier.padding(6.dp),
						onClick = { clickedMovieItem.value = it }
					)
				}
			}

			if (clickedMovieItem.value != null) {
				val clickedItemIsMovie = clickedMovieItem.value!!.title != null

				MovieDetailsSheet(
					movie = clickedMovieItem.value!!,
					genreList = if (clickedItemIsMovie) data.movieGenres else data.seriesGenres,
					viewMore = {},
					closeSheet = { clickedMovieItem.value = null }
				)
			}
		}
	}
}