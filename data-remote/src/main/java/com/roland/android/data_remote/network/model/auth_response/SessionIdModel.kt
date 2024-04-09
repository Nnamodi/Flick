package com.roland.android.data_remote.network.model.auth_response

import com.squareup.moshi.Json

data class SessionIdResponseModel(
	@Json(name = "success")
	val success: Boolean = false,
	@Json(name = "session_id")
	val sessionId: String = ""
)

data class SessionIdModel(
	@Json(name = "session_id")
	val sessionId: String = ""
)
