package com.roland.android.flick.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.filter
import com.roland.android.data_remote.utils.Constants.DEFAULT_PATTERN
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.Video
import com.roland.android.domain.usecase.Category
import com.roland.android.flick.R
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.utils.Constants.DAY
import com.roland.android.flick.utils.Constants.RELEASE_DATE_PATTERN
import com.roland.android.flick.utils.Constants.TRAILER
import com.roland.android.flick.utils.Constants.YOUTUBE
import kotlinx.coroutines.flow.MutableStateFlow
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Extensions {

	fun Double.roundOff(): String {
		val decimalFormat = DecimalFormat("#.#")
		decimalFormat.roundingMode = RoundingMode.CEILING
		val value = decimalFormat.format(this)
		return if ('.' !in value) "$value.0" else value
	}

	fun List<Int>.genres(genreList: GenreList): String {
		return map { id ->
			genreList.genres.find { it.id == id }?.name
		}.joinToString(", ")
	}

	fun Movie.releaseDateRes(): Int {
		return when {
			16 in genreIds && title != null -> R.string.anime_release_date
			title != null -> R.string.release_date
			16 in genreIds && title == null -> R.string.anime_first_air_date
			else -> R.string.first_air_date
		}
	}

	fun String.dateFormat(pattern: String = RELEASE_DATE_PATTERN): String {
		val dateFormat = SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault())
		val parsedDate = dateFormat.parse(this)
		val formatter = SimpleDateFormat(pattern, Locale.getDefault())
		if (pattern != RELEASE_DATE_PATTERN) {
			return parsedDate?.let { formatter.format(it) } ?: this
		}

		val weekFormatter = SimpleDateFormat(DAY, Locale.getDefault())
		val thisWeek = Calendar.getInstance()
		thisWeek.add(Calendar.WEEK_OF_MONTH, 1)

		return parsedDate?.let {
			if (parsedDate <= thisWeek.time) weekFormatter.format(it) else formatter.format(it)
		} ?: this
	}

	fun String.getName(): Int {
		return when (Category.valueOf(this)) {
			Category.TRENDING_MOVIES -> R.string.trending_movies
			Category.TRENDING_SERIES -> R.string.trending_series
			Category.IN_THEATRES -> R.string.movies_in_theatres
			Category.NEW_RELEASES -> R.string.newly_released_series
			Category.TOP_RATED_MOVIES -> R.string.top_rated_movies
			Category.TOP_RATED_SERIES -> R.string.top_rated_series
			Category.ANIME -> R.string.anime
			Category.ANIME_SERIES -> R.string.anime_series
			Category.BOLLYWOOD_MOVIES -> R.string.bollywood_movies
			Category.BOLLYWOOD_SERIES -> R.string.bollywood_series
			Category.POPULAR_MOVIES -> R.string.popular_movies
			Category.POPULAR_SERIES -> R.string.popular_series
		}
	}

	fun String.refine() = when {
		"UnknownHostException" in this -> "No Internet Connection"
		"SocketTimeoutException" in this -> "Connection Timeout"
		"timeout" in this -> "Connection Timeout"
		"ConnectException" in this -> "Connection Interrupted"
		"Unable to resolve host" in this -> "Connection Interrupted"
		else -> this
	}

	fun PagingData<Movie>.refactor(showsVoteAverage: Boolean = true): MutableStateFlow<PagingData<Movie>> {
		return MutableStateFlow(
			filter {
				if (showsVoteAverage) {
					it.voteAverage > 5.0 &&
						it.posterPath != null &&
							it.backdropPath != null
				} else {
					it.posterPath != null && it.backdropPath != null
				}
			}
		)
	}

	fun List<Movie>.refactor(): List<Movie> = filter {
		it.voteAverage > 5.0 &&
			it.posterPath != null &&
				it.backdropPath != null
	}

	fun List<Video>.getTrailerKey(): String? = getTrailer()?.key

	fun List<Video>.getTrailer(): Video? {
		val trailers = filter { video ->
			video.type == TRAILER && video.site == YOUTUBE
		}
		return when {
			isEmpty() -> null
			trailers.isNotEmpty() -> trailers[0]
			else -> this[0]
		}
	}

	@Composable
	fun LazyPagingItems<Movie>.loadStateUi(
		posterType: PosterType,
		largeBoxItemModifier: Modifier = Modifier,
		error: @Composable (String?) -> Unit = {}
	) = apply {
		val placeholder: @Composable (Boolean) -> Unit = { isLoading ->
			when (posterType) {
				PosterType.Small -> repeat(10) {
					SmallBoxItem(isLoading)
				}
				PosterType.Large -> LargeBoxItem(isLoading, largeBoxItemModifier)
				PosterType.ComingSoon -> ComingSoonBoxItem(isLoading, largeBoxItemModifier)
				else -> {}
			}
		}

		when (loadState.refresh) {
			is LoadState.Loading -> {
				placeholder(true)
			}
			is LoadState.Error -> {
				placeholder(false)
				val errorMessage = (loadState.refresh as LoadState.Error).error.localizedMessage
				error(errorMessage?.refine())
			}
			else -> {}
		}
	}

	@Composable
	fun LazyPagingItems<Movie>.appendStateUi() = apply {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(20.dp)
				.padding(top = 50.dp),
			horizontalArrangement = Arrangement.Center
		) {
			when (loadState.append) {
				is LoadState.Loading -> {
					CircularProgressIndicator(Modifier.size(30.dp))
				}
				is LoadState.Error -> {
					OutlinedButton(onClick = { retry() }) {
						Text(stringResource(R.string.more))
					}
				}
				else -> {}
			}
		}
	}

}