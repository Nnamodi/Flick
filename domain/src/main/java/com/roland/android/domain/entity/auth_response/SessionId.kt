package com.roland.android.domain.entity.auth_response

data class SessionIdResponse(
	val success: Boolean = false,
	val sessionId: String = ""
)

data class SessionId(
	val sessionId: String = ""
)
