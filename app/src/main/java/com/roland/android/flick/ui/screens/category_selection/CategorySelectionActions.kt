package com.roland.android.flick.ui.screens.category_selection

import com.roland.android.domain.usecase.Collection

sealed class CategorySelectionActions {

	object Retry : CategorySelectionActions()

	data class LoadMovieList(
		val genreIdList: List<String>,
		val collection: Collection
	) : CategorySelectionActions()

}
