package com.roland.android.domain.usecase

import com.roland.android.domain.entity.auth_response.FavoriteMediaRequest
import com.roland.android.domain.entity.auth_response.RateMediaRequest
import com.roland.android.domain.entity.auth_response.WatchlistMediaRequest
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.LocalConstant.Movies
import com.roland.android.domain.usecase.MediaActions.DeleteRating
import com.roland.android.domain.usecase.MediaActions.Favorite
import com.roland.android.domain.usecase.MediaActions.Rate
import com.roland.android.domain.usecase.MediaActions.Watchlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaUtilUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository,
	private val tvShowRepository: TvShowRepository
) : UseCase<MediaUtilUseCase.Request, MediaUtilUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> {
		return when (request.mediaActions) {
			is Favorite -> {
				favoriteMedia(
					accountId = request.accountId,
					mediaType = request.mediaType,
					request = FavoriteMediaRequest(
						mediaId = request.mediaActions.mediaId,
						mediaType = request.mediaType,
						favorite = request.mediaActions.favorite
					)
				)
			}
			is Watchlist -> {
				watchlistMedia(
					accountId = request.accountId,
					mediaType = request.mediaType,
					request = WatchlistMediaRequest(
						mediaId = request.mediaActions.mediaId,
						mediaType = request.mediaType,
						watchlist = request.mediaActions.watchlist
					)
				)
			}
			is Rate -> {
				rateMedia(
					mediaId = request.mediaActions.mediaId,
					mediaType = request.mediaType,
					request = RateMediaRequest(request.mediaActions.rateValue)
				)
			}
			is DeleteRating -> {
				deleteMediaRating(
					mediaId = request.mediaActions.mediaId,
					mediaType = request.mediaType
				)
			}
		}.map { Response(it) }
	}

	private fun favoriteMedia(
		accountId: Int,
		mediaType: String,
		request: FavoriteMediaRequest
	) = if (mediaType == Movies) {
		movieRepository.favoriteMovie(accountId, request)
	} else {
		tvShowRepository.favoriteTvShow(accountId, request)
	}

	private fun watchlistMedia(
		accountId: Int,
		mediaType: String,
		request: WatchlistMediaRequest
	) = if (mediaType == Movies) {
		movieRepository.watchlistMovie(accountId, request)
	} else {
		tvShowRepository.watchlistTvShow(accountId, request)
	}

	private fun rateMedia(
		mediaId: Int,
		mediaType: String,
		request: RateMediaRequest
	) = if (mediaType == Movies) {
		movieRepository.rateMovie(mediaId, request)
	} else {
		tvShowRepository.rateTvShow(mediaId, request)
	}

	private fun deleteMediaRating(
		mediaId: Int,
		mediaType: String
	) = if (mediaType == Movies) {
		movieRepository.deleteMovieRating(mediaId)
	} else {
		tvShowRepository.deleteTvShowRating(mediaId)
	}

	data class Request(
		val accountId: Int = 0,
		val mediaType: String,
		val mediaActions: MediaActions
	) : UseCase.Request

	data class Response(
		val response: com.roland.android.domain.entity.auth_response.Response
	) : UseCase.Response

}

sealed class MediaActions {

	data class Favorite(
		val mediaId: Int,
		val favorite: Boolean
	) : MediaActions()

	data class Watchlist(
		val mediaId: Int,
		val watchlist: Boolean
	) : MediaActions()

	data class Rate(
		val mediaId: Int,
		val rateValue: Float
	) : MediaActions()

	data class DeleteRating(val mediaId: Int) : MediaActions()

}

private object LocalConstant {
	const val Movies = "movie"
}
