package com.roland.android.domain.entity.auth_response

data class AccessTokenResponse(
	val accountId: String = "",
	val accessToken: String = "",
	val success: Boolean = false,
	val statusMessage: String = "",
	val statusCode: Int = 0
)

data class AccessToken(
	val accessToken: String = ""
)
