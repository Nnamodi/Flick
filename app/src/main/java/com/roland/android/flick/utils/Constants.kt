package com.roland.android.flick.utils

import androidx.compose.ui.unit.dp

object Constants {

	// Image base urls
	const val MOVIE_IMAGE_BASE_URL_W342 = "https://image.tmdb.org/t/p/w342"
	const val MOVIE_IMAGE_BASE_URL_W500 = "https://image.tmdb.org/t/p/w500"
	const val MOVIE_IMAGE_BASE_URL_W780 = "https://image.tmdb.org/t/p/w780"
	const val CAST_IMAGE_BASE_URL_W185 = "https://image.tmdb.org/t/p/w185"
	const val CAST_IMAGE_BASE_URL_W342 = "https://image.tmdb.org/t/p/w342"

	// Other urls
	const val IMDB_BASE_URL = "https://www.imdb.com/title/"
	const val YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com/watch?v="
//	const val VIMEO_BASE_URL = "https://vimeo.com/"
	const val TOKEN_AUTHORIZATION_URL = "https://www.themoviedb.org/auth/access?request_token="
	const val ACCOUNT_SETTINGS_URL = "https://www.themoviedb.org/settings/account"
	const val EDIT_PROFILE_URL = "https://www.themoviedb.org/settings/profile"

	// Media types
	const val MOVIES = "movie"
	const val SERIES = "tv"

	// Video
	const val TRAILER = "Trailer"
	const val YOUTUBE = "YouTube"

	// Date patterns
	const val RELEASE_DATE_PATTERN = "MMMM d, yyyy"
	const val DAY = "EEEE"
	const val YEAR = "yyyy"

	// Dimensions
	val POSTER_WIDTH_X_LARGE = 280.dp
	val POSTER_HEIGHT_X_LARGE = 400.dp
	val POSTER_WIDTH_LARGE = 250.dp
	val POSTER_HEIGHT_LARGE = 370.dp
	val POSTER_WIDTH_MEDIUM = 130.dp
	val POSTER_HEIGHT_MEDIUM = 250.dp
	val POSTER_WIDTH_SMALL = 130.dp
	val POSTER_HEIGHT_SMALL = 195.dp
	val PADDING_WIDTH = 16.dp
	val ROUNDED_EDGE = 12.dp

	val NavigationBarHeight = 80.dp
	val NavigationRailWidth = 80.dp

	const val SCREEN_WIDTH_DIVISOR = 0.5
	const val SCREEN_HEIGHT_DIVISOR = 0.7
}