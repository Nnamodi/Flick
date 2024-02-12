package com.roland.android.flick.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.roland.android.flick.R

private val Rubik = FontFamily(
	Font(R.font.rubik_light, FontWeight.Light),
	Font(R.font.rubik_regular, FontWeight.Normal),
	Font(R.font.rubik_medium, FontWeight.Medium),
	Font(R.font.rubik_black, FontWeight.Black),
	Font(R.font.rubik_semi_bold, FontWeight.SemiBold),
	Font(R.font.rubik_bold, FontWeight.Bold),
	Font(R.font.rubik_extra_bold, FontWeight.ExtraBold)
)

val Typography = Typography(
	displayLarge = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Normal,
		fontSize = 57.sp,
		lineHeight = 64.sp,
		letterSpacing = 0.sp
	),
	displayMedium = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Normal,
		fontSize = 45.sp,
		lineHeight = 52.sp,
		letterSpacing = 0.sp
	),
	displaySmall = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Normal,
		fontSize = 36.sp,
		lineHeight = 44.sp,
		letterSpacing = 0.sp
	),
	headlineLarge = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Normal,
		fontSize = 32.sp,
		lineHeight = 40.sp,
		letterSpacing = 0.sp
	),
	headlineMedium = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Normal,
		fontSize = 28.sp,
		lineHeight = 36.sp,
		letterSpacing = 0.sp
	),
	headlineSmall = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Normal,
		fontSize = 24.sp,
		lineHeight = 32.sp,
		letterSpacing = 0.sp
	),
	titleLarge = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Medium,
		fontSize = 22.sp,
		lineHeight = 28.sp,
		letterSpacing = 0.sp
	),
	titleMedium = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Medium,
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.15.sp
	),
	titleSmall = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.1.sp
	),
	bodyLarge = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Normal,
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.5.sp
	),
	bodyMedium = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.25.sp
	),
	bodySmall = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Light,
		fontSize = 12.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.4.sp
	),
	labelLarge = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.1.sp
	),
	labelMedium = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Medium,
		fontSize = 12.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.5.sp
	),
	labelSmall = TextStyle(
		fontFamily = Rubik,
		fontWeight = FontWeight.Medium,
		fontSize = 11.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.5.sp
	)
)