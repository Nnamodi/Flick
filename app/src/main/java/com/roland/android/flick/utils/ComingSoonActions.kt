package com.roland.android.flick.utils

sealed class ComingSoonActions {

	object Retry : ComingSoonActions()

	data class ToggleCategory(val category: String) : ComingSoonActions()

}
