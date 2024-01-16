package com.roland.android.flick.utils

sealed class HomeActions {

	object Retry : HomeActions()

	data class ToggleCategory(val category: String) : HomeActions()

}
