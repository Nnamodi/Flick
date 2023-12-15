package com.roland.android.data_repository.repository

import com.roland.android.data_repository.data_source.RemoteTvShowSource
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvShowRepositoryImpl @Inject constructor(
	private val remoteTvShowSource: RemoteTvShowSource
) : TvShowRepository {

	override fun fetchTopRatedShows(): Flow<MovieList> = remoteTvShowSource.fetchTopRatedShows()

	override fun fetchPopularShows(): Flow<MovieList> = remoteTvShowSource.fetchPopularShows()

	override fun fetchShowsAiringToday(): Flow<MovieList> = remoteTvShowSource.fetchShowsAiringToday()

	override fun fetchShowsSoonToAir(): Flow<MovieList> = remoteTvShowSource.fetchShowsSoonToAir()

	override fun fetchRecommendedTvShows(seriesId: Int): Flow<MovieList> = remoteTvShowSource.fetchRecommendedTvShows(seriesId)

	override fun fetchSimilarTvShows(seriesId: Int): Flow<MovieList> = remoteTvShowSource.fetchSimilarTvShows(seriesId)

	override fun fetchTvShowDetails(seriesId: Int): Flow<Series> = remoteTvShowSource.fetchTvShowDetails(seriesId)

	override fun fetchSeasonDetails(
		seriesId: Int,
		seasonNumber: Int
	): Flow<Season> = remoteTvShowSource.fetchSeasonDetails(
		seriesId,
		seasonNumber
	)

	override fun fetchEpisodeDetails(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
	): Flow<Episode> = remoteTvShowSource.fetchEpisodeDetails(
		seriesId,
		seasonNumber,
		episodeNumber
	)

	override fun fetchTvShowGenres(): Flow<List<Genre>> = remoteTvShowSource.fetchTvShowGenres()

}