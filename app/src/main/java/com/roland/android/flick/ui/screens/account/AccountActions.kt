package com.roland.android.flick.ui.screens.account

sealed class AccountActions {

	data class UnFavoriteMedia(
		val mediaId: Int,
		val mediaType: String
	) : AccountActions()

	data class RemoveFromWatchlist(
		val mediaId: Int,
		val mediaType: String
	) : AccountActions()

	data class DeleteMediaRating(
		val mediaId: Int,
		val mediaType: String
	) : AccountActions()

	object ReloadMedia : AccountActions()

}
