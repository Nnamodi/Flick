package com.roland.android.data_remote.utils

import com.roland.android.data_remote.network.model.CastModel
import com.roland.android.data_remote.network.model.DatesModel
import com.roland.android.data_remote.network.model.EpisodeModel
import com.roland.android.data_remote.network.model.GenreListModel
import com.roland.android.data_remote.network.model.GenreModel
import com.roland.android.data_remote.network.model.LanguageModel
import com.roland.android.data_remote.network.model.MovieCreditsModel
import com.roland.android.data_remote.network.model.MovieDetailsModel
import com.roland.android.data_remote.network.model.MovieListModel
import com.roland.android.data_remote.network.model.MovieModel
import com.roland.android.data_remote.network.model.ProductionCompanyModel
import com.roland.android.data_remote.network.model.ProductionCountryModel
import com.roland.android.data_remote.network.model.SeasonModel
import com.roland.android.data_remote.network.model.SeriesModel
import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.Dates
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Language
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.entity.ProductionCompany
import com.roland.android.domain.entity.ProductionCountry
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series

object Converters {

	fun convertToMovieList(movieListModel: MovieListModel) = MovieList(
		convertToDates(movieListModel.dates),
		movieListModel.page,
		movieListModel.results.map { convertToMovie(it) },
		movieListModel.totalPages,
		movieListModel.totalResults
	)

	fun convertToMovieDetails(detailsModel: MovieDetailsModel) = MovieDetails(
		detailsModel.id,
		detailsModel.title,
		detailsModel.overview,
		detailsModel.imdbId,
		detailsModel.genres.map { convertToGenre(it) },
		detailsModel.backdropPath,
		detailsModel.posterPath,
		detailsModel.language,
		detailsModel.popularity,
		detailsModel.budget,
		detailsModel.homePage,
		detailsModel.movieType,
		detailsModel.videoAvailable,
		detailsModel.voteAverage,
		detailsModel.voteCount,
		detailsModel.adult,
		detailsModel.productionCompanies.map { convertToProductionCompany(it) },
		detailsModel.productionCountries.map { convertToProductionCountry(it) },
		detailsModel.releaseDate,
		detailsModel.revenue,
		detailsModel.spokenLanguages.map { convertToLanguage(it) },
		detailsModel.status,
		detailsModel.tagline,
		detailsModel.tvName,
		detailsModel.firstAirDate,
		detailsModel.originalCountry
	)

	fun convertToShowDetails(seriesModel: SeriesModel) = Series(
		seriesModel.id,
		seriesModel.name,
		seriesModel.overview,
		seriesModel.backdropPath,
		seriesModel.adult,
		seriesModel.createdBy.map { convertToCast(it) },
		seriesModel.firstAirDate,
		seriesModel.lastAirDate,
		seriesModel.genres.map { convertToGenre(it) },
		seriesModel.homePage,
		seriesModel.inProduction,
		seriesModel.languages,
		convertToEpisode(seriesModel.lastEpisodeToAir),
		convertToEpisode(seriesModel.nextEpisodeToAir),
		seriesModel.networks.map { convertToProductionCompany(it) },
		seriesModel.numberOfEpisodes,
		seriesModel.numberOfSeasons,
		seriesModel.originCountry,
		seriesModel.popularity,
		seriesModel.posterPath,
		seriesModel.productionCompanies.map { convertToProductionCompany(it) },
		seriesModel.productionCountries.map { convertToProductionCountry(it) },
		seriesModel.seasons.map { convertToSeason(it) },
		seriesModel.spokenLanguage.map { convertToLanguage(it) },
		seriesModel.status,
		seriesModel.tagline,
		seriesModel.type,
		seriesModel.voteAverage,
		seriesModel.voteCount
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
		creditsModel.id,
		creditsModel.casts.map { convertToCast(it) }
	)

	fun convertToCast(castModel: CastModel) = Cast(
		castModel.id,
		castModel.name,
		castModel.gender,
		castModel.knownForDepartment,
		castModel.profilePath,
		castModel.popularity,
		castModel.moviesActed.map { convertToMovie(it) },
		castModel.adult,
		castModel.alsoKnownAs,
		castModel.biography,
		castModel.birthDay,
		castModel.deathDay
	)

	fun convertToGenreList(genreListModel: GenreListModel) = GenreList(
		genreListModel.genres.map { convertToGenre(it) }
	)

	fun convertToGenre(genreModel: GenreModel) = Genre(
		genreModel.id,
		genreModel.name
	)

	private fun convertToMovie(movieModel: MovieModel) = Movie(
		movieModel.id,
		movieModel.title,
		movieModel.overview,
		movieModel.genreIds, // convert to genres
		movieModel.backdropPath,
		movieModel.posterPath,
		movieModel.language,
		movieModel.popularity,
		movieModel.movieType,
		movieModel.videoAvailable,
		movieModel.voteAverage,
		movieModel.voteCount,
		movieModel.adult,
		movieModel.releaseDate,
		movieModel.tvName,
		movieModel.firstAirDate,
		movieModel.originCountry
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

}