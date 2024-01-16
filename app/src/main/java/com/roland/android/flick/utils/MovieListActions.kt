package com.roland.android.flick.utils

import com.roland.android.domain.usecase.Category

sealed class MovieListActions {

	data class Retry(val categoryName: String) : MovieListActions()

	object PrepareScreen : MovieListActions()

	data class LoadMovieList(val category: Category) : MovieListActions()

}
