package com.roland.android.flick.utils

import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.usecase.MediaActions
import com.roland.android.domain.usecase.MediaCategory
import com.roland.android.domain.usecase.MediaUtilUseCase
import com.roland.android.flick.models.updatedMediaCategory
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
		sessionId: String,
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
					sessionId = sessionId,
					mediaActions = MediaActions.Favorite(mediaId, favorite)
				)
			)
				.map { converter.convertResponse(it) }
				.collect {
					result(it)
					if (it !is State.Success) return@collect
					updatedMediaCategory.value = MediaCategory.Favorited
				}
		}
	}

	fun watchlistMedia(
		accountId: Int,
		sessionId: String,
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
					sessionId = sessionId,
					mediaActions = MediaActions.Watchlist(mediaId, watchlist)
				)
			)
				.map { converter.convertResponse(it) }
				.collect {
					result(it)
					if (it !is State.Success) return@collect
					updatedMediaCategory.value = MediaCategory.Watchlisted
				}
		}
	}

}