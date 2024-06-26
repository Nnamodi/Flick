package com.roland.android.data_remote.utils

import com.roland.android.data_remote.network.model.CastDetailsModel
import com.roland.android.data_remote.network.model.CastModel
import com.roland.android.data_remote.network.model.DatesModel
import com.roland.android.data_remote.network.model.EpisodeModel
import com.roland.android.data_remote.network.model.ExternalIdsModel
import com.roland.android.data_remote.network.model.GenreModel
import com.roland.android.data_remote.network.model.LanguageModel
import com.roland.android.data_remote.network.model.MovieCreditsModel
import com.roland.android.data_remote.network.model.MovieDetailsModel
import com.roland.android.data_remote.network.model.MovieListModel
import com.roland.android.data_remote.network.model.MovieModel
import com.roland.android.data_remote.network.model.MultiListModel
import com.roland.android.data_remote.network.model.MultiModel
import com.roland.android.data_remote.network.model.ProductionCompanyModel
import com.roland.android.data_remote.network.model.ProductionCountryModel
import com.roland.android.data_remote.network.model.SeasonModel
import com.roland.android.data_remote.network.model.SeriesModel
import com.roland.android.data_remote.network.model.VideoModel
import com.roland.android.data_remote.network.model.auth_response.AccessTokenModel
import com.roland.android.data_remote.network.model.auth_response.AccessTokenResponseModel
import com.roland.android.data_remote.network.model.auth_response.AccountDetailsModel
import com.roland.android.data_remote.network.model.auth_response.AvatarModel
import com.roland.android.data_remote.network.model.auth_response.FavoriteMediaRequestModel
import com.roland.android.data_remote.network.model.auth_response.GravatarModel
import com.roland.android.data_remote.network.model.auth_response.RateMediaRequestModel
import com.roland.android.data_remote.network.model.auth_response.RequestTokenModel
import com.roland.android.data_remote.network.model.auth_response.RequestTokenResponseModel
import com.roland.android.data_remote.network.model.auth_response.ResponseModel
import com.roland.android.data_remote.network.model.auth_response.SessionIdModel
import com.roland.android.data_remote.network.model.auth_response.SessionIdResponseModel
import com.roland.android.data_remote.network.model.auth_response.TmdbAvatarModel
import com.roland.android.data_remote.network.model.auth_response.WatchlistMediaRequestModel
import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.CastDetails
import com.roland.android.domain.entity.Dates
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.ExternalIds
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Language
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.entity.ProductionCompany
import com.roland.android.domain.entity.ProductionCountry
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import com.roland.android.domain.entity.Video
import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.AccessTokenResponse
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.Avatar
import com.roland.android.domain.entity.auth_response.FavoriteMediaRequest
import com.roland.android.domain.entity.auth_response.Gravatar
import com.roland.android.domain.entity.auth_response.RateMediaRequest
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.domain.entity.auth_response.RequestTokenResponse
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.SessionId
import com.roland.android.domain.entity.auth_response.SessionIdResponse
import com.roland.android.domain.entity.auth_response.TmdbAvatar
import com.roland.android.domain.entity.auth_response.WatchlistMediaRequest

object Converters {

	fun convertToMovieList(movieListModel: MovieListModel) = MovieList(
		convertToDates(movieListModel.dates),
		movieListModel.page,
		movieListModel.results.map { convertToMovie(it) },
		movieListModel.totalPages,
		movieListModel.totalResults
	)

	fun convertToMovieList(multiListModel: MultiListModel) = MovieList(
		null,
		multiListModel.page,
		multiListModel.results
			.filter { it.mediaType != "person" }
			.map { convertToMovie(it) },
		multiListModel.totalPages,
		multiListModel.totalResults
	)

	fun convertToMovieDetails(detailsModel: MovieDetailsModel) = MovieDetails(
		detailsModel.id,
		detailsModel.title,
		detailsModel.overview,
		detailsModel.imdbId,
		detailsModel.genres.map { convertToGenre(it) },
		detailsModel.backdropPath ?: "",
		detailsModel.posterPath ?: "",
		detailsModel.language,
		detailsModel.popularity,
		detailsModel.budget,
		detailsModel.homePage,
		detailsModel.videoAvailable,
		detailsModel.voteAverage,
		detailsModel.voteCount,
		detailsModel.adult,
		detailsModel.productionCompanies.map { convertToProductionCompany(it) },
		detailsModel.productionCountries.map { convertToProductionCountry(it) },
		detailsModel.releaseDate,
		detailsModel.runtime,
		detailsModel.spokenLanguages.map { convertToLanguage(it) },
		detailsModel.status,
		detailsModel.tagline,
		detailsModel.videos.results.map { convertToVideo(it) },
		convertToMovieCredits(detailsModel.credits)
	)

	fun convertToShowDetails(seriesModel: SeriesModel) = Series(
		seriesModel.id,
		seriesModel.name,
		seriesModel.overview,
		seriesModel.backdropPath ?: "",
		seriesModel.adult,
		seriesModel.createdBy.map { convertToCast(it) },
		convertExternalIds(seriesModel.externalIds),
		seriesModel.firstAirDate,
		seriesModel.lastAirDate,
		seriesModel.genres.map { convertToGenre(it) },
		seriesModel.homePage,
		seriesModel.inProduction,
		seriesModel.languages,
		seriesModel.lastEpisodeToAir?.let { convertToEpisode(it) },
		seriesModel.nextEpisodeToAir?.let { convertToEpisode(it) },
		seriesModel.networks.map { convertToProductionCompany(it) },
		seriesModel.numberOfEpisodes,
		seriesModel.numberOfSeasons,
		seriesModel.originCountry,
		seriesModel.popularity,
		seriesModel.posterPath ?: "",
		seriesModel.productionCompanies.map { convertToProductionCompany(it) },
		seriesModel.productionCountries.map { convertToProductionCountry(it) },
		seriesModel.seasons.map { convertToSeason(it) },
		seriesModel.spokenLanguage.map { convertToLanguage(it) },
		seriesModel.status,
		seriesModel.tagline,
		seriesModel.type,
		seriesModel.voteAverage,
		seriesModel.voteCount,
		seriesModel.videos.results.map { convertToVideo(it) },
		convertToMovieCredits(seriesModel.credits)
	)

	fun convertToSeason(seasonModel: SeasonModel) = Season(
		seasonModel.id,
		seasonModel.name,
		seasonModel.overview,
		seasonModel.airDate,
		seasonModel.seasonNumber,
		seasonModel.episodeCount,
		seasonModel.episodes?.map { convertToEpisode(it) },
		seasonModel.posterPath,
		seasonModel.voteAverage
	)

	fun convertToEpisode(episodeModel: EpisodeModel) = Episode(
		episodeModel.id,
		episodeModel.name,
		episodeModel.overview,
		episodeModel.airDate,
		episodeModel.episodeNumber,
		episodeModel.episodeType,
		episodeModel.productionCode,
		episodeModel.seasonNumber,
		episodeModel.showId,
		episodeModel.stillPath,
		episodeModel.voteAverage,
		episodeModel.voteCount
	)

	fun convertToMovieCredits(creditsModel: MovieCreditsModel) = MovieCredits(
		creditsModel.cast.map { convertToCast(it) }
	)

	fun convertToCastDetails(castDetailsModel: CastDetailsModel) = CastDetails(
		castDetailsModel.id,
		castDetailsModel.name,
		castDetailsModel.gender,
		castDetailsModel.knownForDepartment,
		castDetailsModel.profilePath,
		castDetailsModel.popularity,
		castDetailsModel.adult,
		castDetailsModel.alsoKnownAs,
		castDetailsModel.biography,
		castDetailsModel.birthDay,
		castDetailsModel.deathDay,
		castDetailsModel.combinedCredits.moviesActed.map { convertToMovie(it) }
	)

	fun convertToResponse(responseModel: ResponseModel) = Response(
		responseModel.statusCode,
		responseModel.statusMessage,
		responseModel.success
	)

	fun convertToSessionIdResponse(sessionId: SessionIdResponseModel) = SessionIdResponse(
		sessionId.sessionId,
		sessionId.success
	)

	fun convertToRequestTokenResponse(requestToken: RequestTokenResponseModel) = RequestTokenResponse(
		requestToken.requestToken,
		requestToken.success,
		requestToken.statusMessage,
		requestToken.statusCode,
	)

	fun convertToAccessTokenResponse(accessToken: AccessTokenResponseModel) = AccessTokenResponse(
		accessToken.accountId,
		accessToken.accessToken,
		accessToken.success,
		accessToken.statusMessage,
		accessToken.statusCode,
	)

	fun convertToAccountDetails(accountDetailsModel: AccountDetailsModel) = AccountDetails(
		accountDetailsModel.id,
		convertToAvatar(accountDetailsModel.avatar),
		accountDetailsModel.name.takeIf { it.isNotEmpty() },
		accountDetailsModel.username
	)

	fun convertToGenreList(genreListModel: List<GenreModel>) = genreListModel.map {
		convertToGenre(it)
	}

	private fun convertToGenre(genreModel: GenreModel) = Genre(
		genreModel.id,
		genreModel.name
	)

	private fun convertToMovie(movieModel: MovieModel) = Movie(
		movieModel.id,
		movieModel.title,
		movieModel.overview,
		movieModel.genreIds,
		movieModel.backdropPath,
		movieModel.posterPath,
		movieModel.language,
		movieModel.popularity,
		movieModel.rating,
		movieModel.videoAvailable,
		movieModel.voteAverage,
		movieModel.voteCount,
		movieModel.adult,
		movieModel.releaseDate,
		movieModel.tvName,
		movieModel.firstAirDate,
		movieModel.originCountry
	)

	private fun convertToMovie(multiModel: MultiModel) = Movie(
		multiModel.id,
		multiModel.title,
		multiModel.overview ?: "",
		multiModel.genreIds ?: emptyList(),
		multiModel.backdropPath,
		multiModel.posterPath,
		multiModel.language ?: "",
		multiModel.popularity,
		rating = 0,
		multiModel.videoAvailable,
		multiModel.voteAverage ?: 0.0,
		multiModel.voteCount ?: 0,
		multiModel.adult,
		multiModel.releaseDate,
		multiModel.tvName,
		multiModel.firstAirDate,
		multiModel.originCountry
	)

	private fun convertToCast(castModel: CastModel) = Cast(
		castModel.id,
		castModel.name,
		castModel.profilePath,
		castModel.roles.getOrNull(0)?.character ?: castModel.character,
		castModel.castId,
		castModel.creditId,
		castModel.order
	)

	private fun convertExternalIds(externalIdsModel: ExternalIdsModel) = ExternalIds(
		externalIdsModel.id,
		externalIdsModel.imdbId,
		externalIdsModel.tvdbId,
		externalIdsModel.wikidataId,
		externalIdsModel.facebookId,
		externalIdsModel.instagramId,
		externalIdsModel.twitterId
	)

	private fun convertToVideo(videoModel: VideoModel) = Video(
		videoModel.id,
		videoModel.name,
		videoModel.key,
		videoModel.publishedAt,
		videoModel.site,
		videoModel.size,
		videoModel.type,
		videoModel.official
	)

	private fun convertToAvatar(avatarModel: AvatarModel) = Avatar(
		convertToGravatar(avatarModel.gravatar),
		convertToTmdbAvatar(avatarModel.tmdbAvatar)
	)

	private fun convertToGravatar(gravatarModel: GravatarModel) = Gravatar(
		gravatarModel.hash
	)

	private fun convertToTmdbAvatar(tmdbAvatarModel: TmdbAvatarModel) = TmdbAvatar(
		tmdbAvatarModel.avatarPath
	)

	private fun convertToDates(datesModel: DatesModel?) = Dates(
		datesModel?.maximum ?: "",
		datesModel?.minimum ?: ""
	)

	private fun convertToProductionCompany(model: ProductionCompanyModel) = ProductionCompany(
		model.id,
		model.name,
		model.originCountry,
		model.logoPath
	)

	private fun convertToProductionCountry(model: ProductionCountryModel) = ProductionCountry(
		model.iso,
		model.name
	)

	private fun convertToLanguage(languageModel: LanguageModel) = Language(
		languageModel.iso,
		languageModel.name,
		languageModel.englishName
	)

	fun convertFromAccessToken(accessToken: AccessToken) = AccessTokenModel(
		accessToken.accessToken
	)

	fun convertFromRequestToken(requestToken: RequestToken) = RequestTokenModel(
		requestToken.requestToken
	)

	fun convertFromSessionId(sessionId: SessionId) = SessionIdModel(
		sessionId.sessionId
	)

	fun convertFromFavoriteMediaRequest(favoriteRequest: FavoriteMediaRequest) = FavoriteMediaRequestModel(
		favoriteRequest.mediaId,
		favoriteRequest.mediaType,
		favoriteRequest.favorite
	)

	fun convertFromRateMediaRequest(rateRequest: RateMediaRequest) = RateMediaRequestModel(
		rateRequest.value
	)

	fun convertFromWatchlistMediaRequest(watchlistRequest: WatchlistMediaRequest) = WatchlistMediaRequestModel(
		watchlistRequest.mediaId,
		watchlistRequest.mediaType,
		watchlistRequest.watchlist
	)

}