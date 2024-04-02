package com.roland.android.domain.entity.auth_response

data class Response(
	val statusCode: Int = 0,
	val statusMessage: String = "",
	val success: Boolean? = null
)
