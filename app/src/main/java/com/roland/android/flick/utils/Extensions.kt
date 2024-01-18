package com.roland.android.flick.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.usecase.Category
import com.roland.android.flick.R
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.shimmer.LargeBoxItem
import com.roland.android.flick.ui.shimmer.MediumBoxItem
import java.math.RoundingMode
import java.text.DecimalFormat

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
		"ConnectException" in this -> "Connection Interrupted"
		"Unable to resolve host" in this -> "Connection Interrupted"
		else -> this
	}

	@Composable
	fun LazyPagingItems<Movie>.loadStateUi(
		posterType: PosterType,
		error: @Composable (String?) -> Unit = {}
	) = apply {
		val placeholder: @Composable (Boolean) -> Unit = { isLoading ->
			when (posterType) {
				PosterType.Medium -> {
					MediumBoxItem(isLoading, Modifier.padding(end = 12.dp))
				}
				PosterType.Large -> LargeBoxItem(isLoading)
				else -> {}
			}
		}

		when {
			loadState.refresh is LoadState.Loading -> {
				repeat(10) { placeholder(true) }
			}
			loadState.refresh is LoadState.Error -> {
				repeat(10) { placeholder(false) }
				val errorMessage = (loadState.refresh as LoadState.Error).error.localizedMessage
				error(errorMessage?.refine())
			}
			else -> {}
		}
	}

}