package com.roland.android.domain.entity.auth_response

data class SessionIdResponse(
	val sessionId: String = "",
	val success: Boolean = false
)

data class SessionId(
	val sessionId: String = ""
)
