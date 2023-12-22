package com.roland.android.flick.state

sealed class UiState<T: Any> {

	object Loading : UiState<Nothing>()

	data class Success<T: Any>(val data: T) : UiState<T>()

	data class Error<T: Any>(val errorMessage: String) : UiState<T>()

}
