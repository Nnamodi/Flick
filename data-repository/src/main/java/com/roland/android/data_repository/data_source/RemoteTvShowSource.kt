package com.roland.android.data_repository.data_source

import androidx.paging.PagingData
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import kotlinx.coroutines.flow.Flow

interface RemoteTvShowSource {

	fun fetchTrendingShows(): Flow<PagingData<Movie>>

	fun fetchTopRatedShows(): Flow<PagingData<Movie>>

	fun fetchPopularShows(): Flow<PagingData<Movie>>

	fun fetchShowsAiringToday(): Flow<PagingData<Movie>>

	fun fetchShowsSoonToAir(): Flow<PagingData<Movie>>

	fun fetchAnimeShows(): Flow<PagingData<Movie>>

	fun fetchBollywoodShows(): Flow<PagingData<Movie>>

	fun fetchRecommendedTvShows(seriesId: Int): Flow<PagingData<Movie>>

	fun fetchSimilarTvShows(seriesId: Int): Flow<PagingData<Movie>>

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

	fun fetchTvShowGenres(): Flow<GenreList>

}
