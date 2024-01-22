package com.roland.android.flick.utils

import com.roland.android.domain.usecase.SearchCategory

sealed class SearchActions {

	data class Retry(val query: String) : SearchActions()

	data class Search(
		val query: String,
		val category: SearchCategory
	) : SearchActions()

}
