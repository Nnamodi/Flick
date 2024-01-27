package com.roland.android.flick.ui.screens.search

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
