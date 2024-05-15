package com.roland.android.flick.utils

import android.content.Context
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri
import com.roland.android.flick.R

class ChromeTabUtils(private val context: Context) {
	private val defaultColor = CustomTabColorSchemeParams.Builder()
		.setToolbarColor(Color(28, 27, 31).toArgb())
		.build()
	private val intent = CustomTabsIntent.Builder()
		.setDefaultColorSchemeParams(defaultColor)
		.setShowTitle(true)
		.setStartAnimations(context, R.anim.slide_in_bottom, R.anim.popup_exit)
		.setExitAnimations(context, R.anim.popup_enter, R.anim.slide_out_bottom)
		.build()

	fun launchUrl(mediaUrl: String) {
		intent.launchUrl(context, mediaUrl.toUri())
	}
}