package com.roland.android.flick.ui.screens.home

sealed class HomeActions {

	object Retry : HomeActions()

	data class ToggleCategory(val category: String) : HomeActions()

}
