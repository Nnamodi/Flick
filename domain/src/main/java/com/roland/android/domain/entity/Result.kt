package com.roland.android.domain.entity

data class Response(
	val success: Boolean,
	val statusCode: Int,
	val statusMessage: String,
)

sealed class Result<out T : Any> {

	data class Success<out T : Any>(val data: T) : Result<T>()

	class Error(val throwable: Throwable) : Result<Nothing>()

}
