package com.roland.android.flick.utils

sealed class SearchActions {

	data class Retry(val query: String) : SearchActions()

	data class Search(
		val query: String,
		val category: SearchCategory
	) : SearchActions()

	data class ToggleCategory(val searchCategory: SearchCategory) : SearchActions()

}

enum class SearchCategory {
	ALL, MOVIES, TV_SHOWS
}
