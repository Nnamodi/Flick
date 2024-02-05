package com.roland.android.flick.ui.screens.details

sealed class DetailsRequest {

	data class GetMovieDetails(val movieId: Int) : DetailsRequest()

	data class GetTvShowDetails(val seriesId: Int) : DetailsRequest()

	data class GetSeasonDetails(
		val seriesId: Int,
		val seasonNumber: Int
	) : DetailsRequest()

	data class GetCastDetails(val personId: Int) : DetailsRequest()

}
