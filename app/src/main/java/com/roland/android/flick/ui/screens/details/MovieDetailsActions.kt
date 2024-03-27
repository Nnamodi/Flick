package com.roland.android.flick.ui.screens.details

import android.content.Context

sealed class MovieDetailsActions {

	data class Share(val mediaUrl: String, val context: Context) : MovieDetailsActions()

}
