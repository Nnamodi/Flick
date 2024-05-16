package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.MediaType.Movies
import com.roland.android.domain.usecase.MediaType.Shows
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class AccountUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository,
	private val tvShowRepository: TvShowRepository
) : UseCase<AccountUseCase.Request, AccountUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> {
		return when (request.mediaType) {
			Movies -> combine(
				movieRepository.fetchFavoritedMovies(request.accountId, request.sessionId),
				movieRepository.fetchWatchlistedMovies(request.accountId, request.sessionId),
				movieRepository.fetchRatedMovies(request.accountId, request.sessionId),
				movieRepository.fetchMovieGenres()
			) { favorited, watchlisted, rated, genres ->
				Response(
					favoritedMovies = favorited,
					watchlistedMovies = watchlisted,
					ratedMovies = rated,
					movieGenres = genres
				)
			}
			Shows -> combine(
				tvShowRepository.fetchFavoritedTvShows(request.accountId, request.sessionId),
				tvShowRepository.fetchWatchlistedTvShows(request.accountId, request.sessionId),
				tvShowRepository.fetchRatedTvShows(request.accountId, request.sessionId),
				tvShowRepository.fetchTvShowGenres()
			) { favorited, watchlisted, rated, genres ->
				Response(
					favoritedShows = favorited,
					watchlistedShows = watchlisted,
					ratedShows = rated,
					showGenres = genres
				)
			}
		}
	}

	data class Request(
		val accountId: Int,
		val sessionId: String,
		val mediaType: MediaType
	) : UseCase.Request

	data class Response(
		val favoritedMovies: PagingData<Movie> = PagingData.empty(),
		val watchlistedMovies: PagingData<Movie> = PagingData.empty(),
		val ratedMovies: PagingData<Movie> = PagingData.empty(),
		val movieGenres: List<Genre> = emptyList(),

		val favoritedShows: PagingData<Movie> = PagingData.empty(),
		val watchlistedShows: PagingData<Movie> = PagingData.empty(),
		val ratedShows: PagingData<Movie> = PagingData.empty(),
		val showGenres: List<Genre> = emptyList()
	) : UseCase.Response

}

enum class MediaType {
	Movies, Shows
}
