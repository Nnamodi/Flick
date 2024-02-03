package com.roland.android.flick.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.R
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_MEDIUM
import com.roland.android.flick.utils.Extensions.appendStateUi
import com.roland.android.flick.utils.Extensions.loadStateUi
import com.roland.android.flick.utils.Extensions.refine
import com.roland.android.flick.utils.MediumBoxItem
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MovieLists(
	paddingValues: PaddingValues = PaddingValues(0.dp),
	scrollState: LazyGridState,
	searchQueryEntered: Boolean = true,
	movies: LazyPagingItems<Movie>,
	onItemClick: (Movie) -> Unit,
	error: @Composable (String?) -> Unit
) {
	val showEmptyList = remember { mutableStateOf(false) }
	val windowSize = rememberWindowSize()
	val dynamicGridSize = if (windowSize.width == WindowType.Landscape) 150.dp else 120.dp

	if (showEmptyList.value) { EmptyList() }

	if (!searchQueryEntered) { NothingSearched() }

	LazyVerticalGrid(
		columns = GridCells.Adaptive(dynamicGridSize),
		modifier = Modifier
			.padding(paddingValues)
			.padding(horizontal = 6.dp),
		state = scrollState,
		contentPadding = PaddingValues(bottom = 78.dp)
	) {
		items(movies.itemCount) { index ->
			movies[index]?.let { movie ->
				MediumItemPoster(
					movie = movie,
					modifier = Modifier
						.padding(6.dp)
						.height(POSTER_HEIGHT_MEDIUM),
					onClick = onItemClick
				)
			}
		}
		movies.apply {
			when (loadState.refresh) {
				is LoadState.Loading -> {
					items(20) {
						MediumBoxItem(
							isLoading = true,
							modifier = Modifier.padding(6.dp)
						)
					}
				}
				is LoadState.Error -> {
					items(20) {
						MediumBoxItem(
							isLoading = false,
							modifier = Modifier.padding(6.dp)
						)
						val errorMessage = (loadState.refresh as LoadState.Error).error.localizedMessage
						error(errorMessage?.refine())
					}
				}
				is LoadState.NotLoading -> {
					showEmptyList.value = itemSnapshotList.isEmpty()
				}
			}
		}
		item {
			movies.appendStateUi()
		}
	}
}

@Composable
fun HorizontalPosters(
	pagingData: MutableStateFlow<PagingData<Movie>>,
	header: String,
	header2: String? = null,
	selectedHeader: Int = 0,
	onHeaderClick: (Int) -> Unit = {},
	onItemClick: (Movie) -> Unit,
	seeMore: () -> Unit
) {
	val movieList = pagingData.collectAsLazyPagingItems()
	val listState = rememberLazyListState()
	val listState2 = rememberLazyListState()

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(bottom = 12.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(PADDING_WIDTH),
			verticalAlignment = Alignment.CenterVertically
		) {
			Header(
				header = header,
				header2 = header2,
				selectedHeader = selectedHeader,
				onHeaderClick = onHeaderClick
			)
			Spacer(Modifier.weight(1f))
			if ((header2 == null) && (movieList.loadState.refresh is LoadState.NotLoading)) {
				Text(
					text = stringResource(R.string.more),
					modifier = Modifier
						.clip(MaterialTheme.shapes.small)
						.clickable { seeMore() }
						.padding(6.dp),
					color = Color.Gray
				)
			}
		}
		LazyRow(
			state = if (selectedHeader == 2) listState2 else listState,
			contentPadding = PaddingValues(
				start = PADDING_WIDTH,
				end = PADDING_WIDTH - 12.dp
			)
		) {
			val movies = movieList.itemSnapshotList.take(20)
			items(movies.size) { index ->
				movies[index]?.let { movie ->
					SmallItemPoster(
						movie = movie,
						modifier = Modifier.padding(end = 12.dp),
						onClick = onItemClick
					)
				}
			}
			item {
				movieList.loadStateUi(PosterType.Small)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun EmptyList() {
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(text = stringResource(R.string.sad_face), fontSize = 100.sp)
		Spacer(Modifier.height(30.dp))
		Text(
			text = stringResource(R.string.no_result),
			fontSize = 24.sp,
			fontWeight = FontWeight.SemiBold
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun NothingSearched() {
	val windowSize = rememberWindowSize()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.alpha(0.8f),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Icon(
			imageVector = Icons.Rounded.Search,
			contentDescription = null,
			modifier = Modifier.size(if (windowSize.width == WindowType.Portrait) 250.dp else 125.dp)
		)
		Spacer(Modifier.height(if (windowSize.width == WindowType.Portrait) 30.dp else 15.dp))
		Text(
			text = stringResource(R.string.nothing_searched),
			fontSize = 24.sp,
			fontWeight = FontWeight.SemiBold
		)
	}
}