package com.roland.android.flick.utils

sealed class HomeScreenActions {

	data class ToggleCategory(val category: String) : HomeScreenActions()

}
