package com.roland.android.domain.entity.auth_response

data class RequestTokenResponse(
	val requestToken: String = "",
	val success: Boolean = false,
	val statusMessage: String = "",
	val statusCode: Int = 0
)

data class RequestToken(
	val requestToken: String = ""
)
