package com.roland.android.flick.utils

import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.usecase.MediaActions
import com.roland.android.domain.usecase.MediaUtilUseCase
import com.roland.android.flick.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaUtil @Inject constructor(
	private val mediaUtilUseCase: MediaUtilUseCase,
	private val converter: ResponseConverter,
	private val coroutineScope: CoroutineScope
) {

	fun favoriteMedia(
		accountId: Int,
		mediaId: Int,
		mediaType: String,
		favorite: Boolean,
		result: (State<Response>) -> Unit
	) {
		coroutineScope.launch {
			mediaUtilUseCase.execute(
				MediaUtilUseCase.Request(
					accountId = accountId,
					mediaType = mediaType,
					mediaActions = MediaActions.Favorite(mediaId, favorite)
				)
			)
				.map { converter.convertResponse(it) }
				.collect { result(it) }
		}
	}

	fun watchlistMedia(
		accountId: Int,
		mediaId: Int,
		mediaType: String,
		watchlist: Boolean,
		result: (State<Response>) -> Unit
	) {
		coroutineScope.launch {
			mediaUtilUseCase.execute(
				MediaUtilUseCase.Request(
					accountId = accountId,
					mediaType = mediaType,
					mediaActions = MediaActions.Watchlist(mediaId, watchlist)
				)
			)
				.map { converter.convertResponse(it) }
				.collect { result(it) }
		}
	}

	fun rateMedia(
		mediaId: Int,
		mediaType: String,
		rateValue: Float,
		result: (State<Response>) -> Unit
	) {
		coroutineScope.launch {
			mediaUtilUseCase.execute(
				MediaUtilUseCase.Request(
					mediaType = mediaType,
					mediaActions = MediaActions.Rate(mediaId, rateValue)
				)
			)
				.map { converter.convertResponse(it) }
				.collect { result(it) }
		}
	}

}