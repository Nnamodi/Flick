package com.roland.android.data_remote.network.model.auth_response

import com.squareup.moshi.Json

data class RequestTokenResponseModel(
	@Json(name = "request_token")
	val requestToken: String = "",
	@Json(name = "success")
	val success: Boolean = false,
	@Json(name = "status_message")
	val statusMessage: String = "",
	@Json(name = "status_code")
	val statusCode: Int = 0
)

data class RequestTokenModel(
	@Json(name = "request_token")
	val requestToken: String = ""
)
