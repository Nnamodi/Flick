package com.roland.android.flick.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.Video
import com.roland.android.flick.R
import com.roland.android.flick.ui.screens.details.VideoPlayer
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_MEDIUM
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_SMALL
import com.roland.android.flick.utils.Extensions.appendStateUi
import com.roland.android.flick.utils.Extensions.loadStateUi
import com.roland.android.flick.utils.Extensions.refine
import com.roland.android.flick.utils.MediumBoxItem
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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

	var scrollIndex by rememberSaveable { mutableIntStateOf(0) }
	var scrollOffset by rememberSaveable { mutableIntStateOf(0) }

	LaunchedEffect(true) {
		if (scrollIndex == 0) return@LaunchedEffect
		scrollState.animateScrollToItem(scrollIndex, scrollOffset)
	}

	DisposableEffect(Unit) {
		onDispose {
			scrollIndex = scrollState.firstVisibleItemIndex
			scrollOffset = scrollState.firstVisibleItemScrollOffset
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
	onMovieClick: (Movie) -> Unit,
	seeMore: () -> Unit
) {
	val movieList = pagingData.collectAsLazyPagingItems()
	val lazyListState = rememberLazyListState()
	var scrollIndex1 by rememberSaveable { mutableIntStateOf(0) }
	var scrollOffset1 by rememberSaveable { mutableIntStateOf(0) }
	var scrollIndex2 by rememberSaveable { mutableIntStateOf(0) }
	var scrollOffset2 by rememberSaveable { mutableIntStateOf(0) }

	Column(Modifier.padding(bottom = 12.dp)) {
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
				onHeaderClick = {
					if (selectedHeader == 1) {
						scrollIndex1 = lazyListState.firstVisibleItemIndex
						scrollOffset1 = lazyListState.firstVisibleItemScrollOffset
					} else {
						scrollIndex2 = lazyListState.firstVisibleItemIndex
						scrollOffset2 = lazyListState.firstVisibleItemScrollOffset
					}
					onHeaderClick(it)
				}
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
			state = lazyListState,
			contentPadding = PaddingValues(
				start = PADDING_WIDTH,
				end = PADDING_WIDTH - 12.dp
			)
		) {
			if (header2 == null) {
				val movies = movieList.itemSnapshotList.take(20)
				items(movies.size) { index ->
					movies[index]?.let { movie ->
						SmallItemPoster(
							movie = movie,
							modifier = Modifier.padding(end = 12.dp),
							onClick = onMovieClick
						)
					}
				}
			} else {
				items(movieList.itemCount) { index ->
					movieList[index]?.let { movie ->
						SmallItemPoster(
							movie = movie,
							modifier = Modifier.padding(end = 12.dp),
							onClick = onMovieClick
						)
					}
				}
			}
			item {
				movieList.loadStateUi(PosterType.Small)
			}
		}
		if ((movieList.itemCount == 0) && (movieList.loadState.refresh is LoadState.NotLoading)) {
			EmptyRow()
		}
	}

	LaunchedEffect(selectedHeader) {
		if (selectedHeader == 1) {
			if (scrollIndex1 == 0) return@LaunchedEffect
			lazyListState.animateScrollToItem(scrollIndex1, scrollOffset1)
		} else {
			if (scrollIndex2 == 0) return@LaunchedEffect
			lazyListState.animateScrollToItem(scrollIndex2, scrollOffset2)
		}
	}
}

@Composable
fun HorizontalPosters(
	movieList: List<Movie>,
	header: String,
	onMovieClick: (Movie) -> Unit
) {
	Column(Modifier.padding(vertical = 12.dp)) {
		Header(
			header = header,
			modifier = Modifier.padding(start = PADDING_WIDTH, end = PADDING_WIDTH, bottom = 12.dp)
		)
		LazyRow(
			contentPadding = PaddingValues(
				start = PADDING_WIDTH,
				end = PADDING_WIDTH - 12.dp
			)
		) {
			items(movieList.size) { index ->
				SmallItemPoster(
					movie = movieList[index],
					modifier = Modifier.padding(end = 12.dp),
					onClick = onMovieClick
				)
			}
		}
		if (movieList.isEmpty()) { EmptyRow() }
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoList(videos: List<Video>) {
	val pagerState = rememberPagerState { videos.size }
	val scope = rememberCoroutineScope()
	val screenWidth = LocalConfiguration.current.screenWidthDp
	val columnModifier = if (videos.isNotEmpty()) {
		Modifier
			.padding(bottom = 12.dp)
			.height(52.dp + POSTER_HEIGHT_SMALL + 12.dp + 42.dp + 16.dp + 16.dp)
	} else Modifier.padding(bottom = 12.dp)

	Column(columnModifier) {
		Header(
			header = stringResource(R.string.more_videos),
			modifier = Modifier.padding(PADDING_WIDTH)
		)
		HorizontalPager(
			state = pagerState,
			contentPadding = PaddingValues(horizontal = PADDING_WIDTH),
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 16.dp),
			beyondBoundsPageCount = videos.size,
			pageSpacing = 8.dp,
			verticalAlignment = Alignment.Top,
			flingBehavior = PagerDefaults.flingBehavior(
				state = pagerState,
				pagerSnapDistance = PagerSnapDistance.atMost(videos.size)
			),
			pageSize = PageSize.Fixed(screenWidth.dp - (PADDING_WIDTH * 2))
		) { page ->
			VideoPlayer(
				video = videos[page],
				modifier = Modifier.fillMaxWidth(),
				canPlay = page == pagerState.currentPage
			)
		}
		Spacer(Modifier.weight(1f))
		HorizontalPagerIndicator(
			pageCount = videos.size,
			pagerState = pagerState,
			onClick = { scope.launch { pagerState.animateScrollToPage(it) } }
		)
		if (videos.isEmpty()) {
			EmptyRow(
				text = stringResource(R.string.no_videos),
				height = POSTER_HEIGHT_SMALL / 2
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HorizontalPagerIndicator(
	pageCount: Int,
	pagerState: PagerState,
	indicatorColor: Color = MaterialTheme.colorScheme.surfaceTint,
	unselectedIndicatorSize: Dp = 8.dp,
	selectedIndicatorSize: Dp = 12.dp,
	onClick: (Int) -> Unit
) {
	val scrollState = rememberScrollState()

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 30.dp)
			.horizontalScroll(scrollState),
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically
	) {
		repeat(pageCount) { index ->
			val (color, size) = if (pagerState.currentPage == index || pagerState.targetPage == index) {
				val pageOffset = ((pagerState.currentPage - index) + pagerState.currentPageOffsetFraction).absoluteValue
				val offsetPercentage = 1f - pageOffset.coerceIn(0f, 1f)
				val size = unselectedIndicatorSize + ((selectedIndicatorSize - unselectedIndicatorSize) * offsetPercentage)
				indicatorColor.copy(alpha = offsetPercentage) to size
			} else {
				indicatorColor.copy(alpha = 0.1f) to unselectedIndicatorSize
			}

			Box(
				modifier = Modifier
					.padding(horizontal = 6.dp)
					.size(width = size, height = size / 2)
					.clip(MaterialTheme.shapes.small)
					.background(color)
					.clickable { onClick(index) }
			)
		}
	}

	LaunchedEffect(pagerState.currentPage) {
		scrollState.animateScrollTo(pagerState.currentPage)
	}
}

@Composable
fun EmptyRow(
	text: String = stringResource(R.string.nothing_here),
	height: Dp = POSTER_HEIGHT_SMALL
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(height),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = text,
			fontStyle = FontStyle.Italic
		)
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