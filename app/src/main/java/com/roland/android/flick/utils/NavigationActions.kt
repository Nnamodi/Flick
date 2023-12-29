package com.roland.android.flick.utils

sealed class NavigationActions {

	data class GetMovieDetails(val movieId: Int) : NavigationActions()

	data class GetTvShowDetails(val seriesId: Int) : NavigationActions()

	data class GetSeasonDetails(
		val seriesId: Int,
		val seasonNumber: Int,
		val episodeNumber: Int
	) : NavigationActions()

	data class GetCastDetails(val personId: Int) : NavigationActions()

}
