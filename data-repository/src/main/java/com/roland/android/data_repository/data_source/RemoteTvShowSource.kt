package com.roland.android.data_repository.data_source

import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import kotlinx.coroutines.flow.Flow

interface RemoteTvShowSource {

	fun fetchTopRatedShows(): Flow<MovieList>

	fun fetchPopularShows(): Flow<MovieList>

	fun fetchShowsAiringToday(): Flow<MovieList>

	fun fetchShowsSoonToAir(): Flow<MovieList>

	fun fetchRecommendedTvShows(seriesId: Int): Flow<MovieList>

	fun fetchSimilarTvShows(seriesId: Int): Flow<MovieList>

	fun fetchTvShowDetails(seriesId: Int): Flow<Series>

	fun fetchSeasonDetails(
		seriesId: Int,
		seasonNumber: Int
	): Flow<Season>

	fun fetchEpisodeDetails(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int
	): Flow<Episode>

	fun fetchTvShowGenres(): Flow<List<Genre>>

}
