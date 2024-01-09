package com.roland.android.flick.utils

import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.R
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

}