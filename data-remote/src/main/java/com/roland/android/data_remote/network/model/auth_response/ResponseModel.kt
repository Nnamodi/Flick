package com.roland.android.data_remote.network.model.auth_response

import com.squareup.moshi.Json

data class ResponseModel(
	@Json(name = "status_code")
	val statusCode: Int = 0,
	@Json(name = "status_message")
	val statusMessage: String = "",
	@Json(name = "success")
	val success: Boolean? = null
)
