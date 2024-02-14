package com.roland.android.data_remote.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Constants {

	const val BASE_URL = "https://api.themoviedb.org/"
	const val MAX_PAGE_SIZE = 20
	const val INITIAL_PAGE = 1

	// Date patterns
	const val DEFAULT_PATTERN = "yyyy-MM-dd"
	const val TOMORROW = Calendar.DAY_OF_YEAR
	const val NEXT_MONTH = Calendar.MONTH

	fun date(span: Int): String {
		val calendar = Calendar.getInstance()
		calendar.add(span, 1)
		val formatter = SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault())
		return formatter.format(calendar.time)
	}
}