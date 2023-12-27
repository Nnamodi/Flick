package com.roland.android.flick.utils

sealed class NavigationActions {

	data class GetMovieDetails(val movieId: Int) : NavigationActions()

}
