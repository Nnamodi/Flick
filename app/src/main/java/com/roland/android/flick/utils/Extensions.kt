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
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.Video
import com.roland.android.domain.usecase.Category
import com.roland.android.domain.usecase.Collection
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

	fun List<Int>.genres(genreList: List<Genre>): String {
		return map { id ->
			genreList.find { it.id == id }?.name
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
		return try {
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
		} catch (e: Exception) { "_" }
	}

	fun String?.isReleased(): Boolean {
		return try {
			val today = Calendar.getInstance().time
			val dateFormat = SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault())
			val releaseDate = dateFormat.parse(this ?: "") ?: today
			releaseDate <= today
		} catch (e: Exception) { true }
	}

	fun String.getName(): Int {
		return when (Category.valueOf(this)) {
			Category.TRENDING_MOVIES -> R.string.trending_movies
			Category.TRENDING_SERIES -> R.string.trending_series
			Category.IN_THEATRES -> R.string.movies_in_theatres
			Category.NEW_RELEASES -> R.string.newly_released_series
			Category.TOP_RATED_MOVIES -> R.string.top_rated_movies
			Category.TOP_RATED_SERIES -> R.string.top_rated_series
			Category.POPULAR_MOVIES -> R.string.popular_movies
			Category.POPULAR_SERIES -> R.string.popular_series
			// account
			Category.MOVIES_FOR_YOU -> R.string.movies_for_you
			Category.SERIES_FOR_YOU -> R.string.series_for_you
			Category.FAVORITED_MOVIES -> R.string.favorited_movies
			Category.FAVORITED_SERIES -> R.string.favorited_series
			Category.WATCHLISTED_MOVIES -> R.string.watchlisted_movies
			Category.WATCHLISTED_SERIES -> R.string.watchlisted_series
			Category.RATED_MOVIES -> R.string.rated_movies
			Category.RATED_SERIES -> R.string.rated_series
			// genre
			Category.ANIME -> R.string.anime
			Category.ANIME_SERIES -> R.string.anime_series
			Category.COMEDY_MOVIES -> R.string.comedy_movies
			Category.COMEDY_SERIES -> R.string.comedy_series
			Category.ROMEDY_MOVIES -> R.string.romedy_movies
			Category.ROMEDY_SERIES -> R.string.romedy_series
			Category.SCI_FI_MOVIES -> R.string.sci_fi_movies
			Category.SCI_FI_SERIES -> R.string.sci_fi_series
			Category.WAR_STORY_MOVIES -> R.string.war_story_movies
			Category.WAR_STORY_SERIES -> R.string.war_story_series
			// region
			Category.NOLLYWOOD_MOVIES -> R.string.nollywood_movies
			Category.NOLLYWOOD_SERIES -> R.string.nollywood_series
			Category.KOREAN_MOVIES -> R.string.korean_movies
			Category.K_DRAMA -> R.string.k_drama
			Category.BOLLYWOOD_MOVIES -> R.string.bollywood_movies
			Category.BOLLYWOOD_SERIES -> R.string.bollywood_series
		}
	}

	fun Collection.getName(): Int {
		return when (this) {
			Collection.MOVIES -> R.string.movies
			Collection.SERIES -> R.string.series
		}
	}

	fun String.refine() = when {
		"UnknownHostException" in this -> "No Internet Connection"
		"Failed to connect" in this -> "No Internet Connection"
		"SocketTimeoutException" in this -> "Connection Timeout"
		"timeout" in this -> "Connection Timeout"
		"ConnectException" in this -> "Connection Interrupted"
		"Unable to resolve host" in this -> "Connection Interrupted"
		else -> this
	}

	fun PagingData<Movie>.refactor(): MutableStateFlow<PagingData<Movie>> {
		return MutableStateFlow(
			filter {
				it.posterPath != null && it.backdropPath != null
			}
		)
	}

	fun List<Movie>.refactor(): List<Movie> = filter {
		it.posterPath != null && it.backdropPath != null
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
				.padding(top = 50.dp),
			horizontalArrangement = Arrangement.Center
		) {
			when (loadState.append) {
				is LoadState.Loading -> {
					CircularProgressIndicator(
						modifier = Modifier
							.padding(20.dp)
							.size(30.dp)
					)
				}
				is LoadState.Error -> {
					OutlinedButton(
						onClick = { retry() },
						modifier = Modifier.padding(20.dp)
					) {
						Text(stringResource(R.string.more))
					}
				}
				else -> {}
			}
		}
	}

}