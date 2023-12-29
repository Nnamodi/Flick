package com.roland.android.flick.state

sealed class State<T: Any> {

	object Loading : State<Nothing>()

	data class Success<T: Any>(val data: T) : State<T>()

	data class Error<T: Any>(val errorMessage: String) : State<T>()

}
