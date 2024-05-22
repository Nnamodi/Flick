package com.roland.android.data_repository

import com.roland.android.data_repository.SampleTestData.animeCollections
import com.roland.android.data_repository.SampleTestData.bollywoodMovies
import com.roland.android.data_repository.SampleTestData.genreList
import com.roland.android.data_repository.SampleTestData.movieDetails
import com.roland.android.data_repository.SampleTestData.nowPlayingMovies
import com.roland.android.data_repository.SampleTestData.popularMovies
import com.roland.android.data_repository.SampleTestData.recommendedMovies
import com.roland.android.data_repository.SampleTestData.similarMovies
import com.roland.android.data_repository.SampleTestData.topRatedMovies
import com.roland.android.data_repository.SampleTestData.trendingMovies
import com.roland.android.data_repository.SampleTestData.upcomingMovies
import com.roland.android.data_repository.data_source.remote.RemoteMovieSource
import com.roland.android.data_repository.repository.MovieRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MovieRepositoryImplTest {

	private val remoteMovieSource = mock<RemoteMovieSource>()
	private val movieRepositoryImpl = MovieRepositoryImpl(remoteMovieSource)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testFetchMovies() = runTest {
		whenever(remoteMovieSource.fetchTrendingMovies()).thenReturn(flowOf(trendingMovies))
		whenever(remoteMovieSource.fetchPopularMovies()).thenReturn(flowOf(popularMovies))
		whenever(remoteMovieSource.fetchNowPlayingMovies()).thenReturn(flowOf(nowPlayingMovies))
		whenever(remoteMovieSource.fetchTopRatedMovies()).thenReturn(flowOf(topRatedMovies))
		whenever(remoteMovieSource.fetchUpcomingMovies()).thenReturn(flowOf(upcomingMovies))
		whenever(remoteMovieSource.fetchMoviesByGenre("")).thenReturn(flowOf(animeCollections))
		whenever(remoteMovieSource.fetchMoviesByRegion("")).thenReturn(flowOf(bollywoodMovies))
		whenever(remoteMovieSource.fetchRecommendedMovies(2)).thenReturn(flowOf(recommendedMovies))
		whenever(remoteMovieSource.fetchSimilarMovies(2)).thenReturn(flowOf(similarMovies))
		whenever(remoteMovieSource.searchMovies("")).thenReturn(flowOf(trendingMovies))
		whenever(remoteMovieSource.fetchMovieDetails(2)).thenReturn(flowOf(movieDetails))
		whenever(remoteMovieSource.fetchMovieGenres()).thenReturn(flowOf(genreList))

		val trending = movieRepositoryImpl.fetchTrendingMovies().first()
		val popular = movieRepositoryImpl.fetchPopularMovies().first()
		val nowPlaying = movieRepositoryImpl.fetchNowPlayingMovies().first()
		val topRated = movieRepositoryImpl.fetchTopRatedMovies().first()
		val upcoming = movieRepositoryImpl.fetchUpcomingMovies().first()
		val anime = movieRepositoryImpl.fetchMoviesByGenre("").first()
		val bollywood = movieRepositoryImpl.fetchMoviesByRegion("").first()
		val recommended = movieRepositoryImpl.fetchRecommendedMovies(2).first()
		val similar = movieRepositoryImpl.fetchSimilarMovies(2).first()
		val search = movieRepositoryImpl.searchMovies("").first()
		val movie = movieRepositoryImpl.fetchMovieDetails(2).first()
		val genres = movieRepositoryImpl.fetchMovieGenres().first()

		val fetchedMovieData = listOf(
			trending,
			popular,
			nowPlaying,
			topRated,
			upcoming,
			anime,
			bollywood,
			recommended,
			similar,
			search,
			movie,
			genres
		)
		val sampleMovieData = listOf(
			trendingMovies,
			popularMovies,
			nowPlayingMovies,
			topRatedMovies,
			upcomingMovies,
			animeCollections,
			bollywoodMovies,
			recommendedMovies,
			similarMovies,
			trendingMovies,
			movieDetails,
			genreList
		)
		assertEquals(fetchedMovieData, sampleMovieData)
	}

}