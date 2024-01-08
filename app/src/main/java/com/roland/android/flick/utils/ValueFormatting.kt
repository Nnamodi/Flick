package com.roland.android.flick.utils

import java.math.RoundingMode
import java.text.DecimalFormat

object ValueFormatting {

	fun Double.roundOff(): String {
		val decimalFormat = DecimalFormat("#.#")
		decimalFormat.roundingMode = RoundingMode.CEILING
		val value = decimalFormat.format(this)
		return if (value.length == 1) "$value.0" else value
	}

}