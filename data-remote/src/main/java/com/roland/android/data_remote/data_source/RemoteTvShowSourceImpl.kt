package com.roland.android.data_remote.data_source

import com.roland.android.data_remote.network.service.TvShowService
import com.roland.android.data_remote.utils.Converters.convertToEpisode
import com.roland.android.data_remote.utils.Converters.convertToGenreList
import com.roland.android.data_remote.utils.Converters.convertToMovieList
import com.roland.android.data_remote.utils.Converters.convertToSeason
import com.roland.android.data_remote.utils.Converters.convertToShowDetails
import com.roland.android.data_repository.data_source.RemoteTvShowSource
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import com.roland.android.domain.entity.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteTvShowSourceImpl @Inject constructor(
	private val tvShowService: TvShowService
) : RemoteTvShowSource {

	override fun fetchTopRatedShows(): Flow<MovieList> = flow {
		emit(tvShowService.fetchTopRatedShows())
	}.map { showListModel ->
		convertToMovieList(showListModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchPopularShows(): Flow<MovieList> = flow {
		emit(tvShowService.fetchPopularShows())
	}.map { showListModel ->
		convertToMovieList(showListModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchShowsAiringToday(): Flow<MovieList> = flow {
		emit(tvShowService.fetchShowsAiringToday())
	}.map { showListModel ->
		convertToMovieList(showListModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchShowsSoonToAir(): Flow<MovieList> = flow {
		emit(tvShowService.fetchShowsSoonToAir())
	}.map { showListModel ->
		convertToMovieList(showListModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchRecommendedTvShows(seriesId: Int): Flow<MovieList> = flow {
		emit(tvShowService.fetchRecommendedTvShows(seriesId))
	}.map { showListModel ->
		convertToMovieList(showListModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchSimilarTvShows(seriesId: Int): Flow<MovieList> = flow {
		emit(tvShowService.fetchSimilarTvShows(seriesId))
	}.map { showListModel ->
		convertToMovieList(showListModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchTvShowDetails(seriesId: Int): Flow<Series> = flow {
		emit(tvShowService.fetchTvShowDetails(seriesId))
	}.map { seriesModel ->
		convertToShowDetails(seriesModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchSeasonDetails(
		seriesId: Int,
		seasonNumber: Int
	): Flow<Season> = flow {
		emit(tvShowService.fetchSeasonDetails(seriesId, seasonNumber))
	}.map { seasonModel ->
		convertToSeason(seasonModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchEpisodeDetails(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
	): Flow<Episode> = flow {
		emit(tvShowService.fetchEpisodeDetails(seriesId, seasonNumber, episodeNumber))
	}.map { episodeModel ->
		convertToEpisode(episodeModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchTvShowGenres(): Flow<GenreList> = flow {
		emit(tvShowService.fetchTvShowGenres())
	}.map { genreListModel ->
		convertToGenreList(genreListModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}
}