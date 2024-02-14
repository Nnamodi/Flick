package com.roland.android.flick.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun FlickTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	enableDynamicColor: Boolean = true, // on Android 12+
	content: @Composable () -> Unit
) {
	val dynamicColor = enableDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
	val colorScheme = when {
		dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
		dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}
	val view = LocalView.current
	if (!view.isInEditMode){
		SideEffect {
			val window = (view.context as Activity).window
			window.statusBarColor = Color.Transparent.toArgb()
			WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
		}
	}

	MaterialTheme(
		colorScheme = colorScheme,
		shapes = Shapes,
		typography = Typography,
		content = content
	)
}