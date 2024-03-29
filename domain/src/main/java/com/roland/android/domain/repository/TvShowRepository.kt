package com.roland.android.domain.repository

import androidx.paging.PagingData
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {

	fun fetchTrendingShows(): Flow<PagingData<Movie>>

	fun fetchTopRatedShows(): Flow<PagingData<Movie>>

	fun fetchPopularShows(): Flow<PagingData<Movie>>

	fun fetchShowsAiringToday(): Flow<PagingData<Movie>>

	fun fetchShowsSoonToAir(): Flow<PagingData<Movie>>

	fun fetchShowsByGenre(genreIds: String): Flow<PagingData<Movie>>

	fun fetchShowsByRegion(region: String): Flow<PagingData<Movie>>

	fun fetchRecommendedTvShows(seriesId: Int): Flow<PagingData<Movie>>

	fun fetchSimilarTvShows(seriesId: Int): Flow<PagingData<Movie>>

	fun searchTvShows(query: String): Flow<PagingData<Movie>>

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
