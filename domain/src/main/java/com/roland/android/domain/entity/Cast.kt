package com.roland.android.domain.entity

data class Cast(
	val id: Int = 0,
	val name: String = "",
	val profilePath: String? = null,
	val character: String = "",
	val castId: Int = 0,
	val creditId: String = "",
	val order: Int = 0
)
