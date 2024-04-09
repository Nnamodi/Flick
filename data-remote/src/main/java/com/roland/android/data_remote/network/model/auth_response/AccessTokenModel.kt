package com.roland.android.data_remote.network.model.auth_response

import com.squareup.moshi.Json

data class AccessTokenResponseModel(
	@Json(name = "account_id")
	val accountId: String = "",
	@Json(name = "access_token")
	val accessToken: String = "",
	@Json(name = "success")
	val success: Boolean = false,
	@Json(name = "status_message")
	val statusMessage: String = "",
	@Json(name = "status_code")
	val statusCode: Int = 0
)

data class AccessTokenModel(
	@Json(name = "access_token")
	val accessToken: String = ""
)
