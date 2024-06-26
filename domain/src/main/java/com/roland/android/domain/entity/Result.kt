package com.roland.android.domain.entity

sealed class Result<out T : Any> {

	data class Success<out T : Any>(val data: T) : Result<T>()

	class Error(val exception: UseCaseException) : Result<Nothing>()

}

sealed class UseCaseException(cause: Throwable) : Throwable(cause) {

	class AuthException(cause: Throwable) : UseCaseException(cause)

	class MovieException(cause: Throwable) : UseCaseException(cause)

	class CastException(cause: Throwable) : UseCaseException(cause)

	class TvShowException(cause: Throwable) : UseCaseException(cause)

	class UnknownException(cause: Throwable) : UseCaseException(cause)

	companion object {
		fun processThrowable(throwable: Throwable): UseCaseException {
			return if (throwable is UseCaseException) throwable else {
				UnknownException(throwable)
			}
		}
	}

}
