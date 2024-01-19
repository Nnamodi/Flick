package com.roland.android.data_remote

import com.roland.android.data_remote.data_source.RemoteMovieSourceImpl
import com.roland.android.data_remote.network.service.MovieService
import com.roland.android.data_remote.sample_data.ExpectedSampleData.animeCollections
import com.roland.android.data_remote.sample_data.ExpectedSampleData.bollywoodMovies
import com.roland.android.data_remote.sample_data.ExpectedSampleData.genreList
import com.roland.android.data_remote.sample_data.ExpectedSampleData.movieDetails
import com.roland.android.data_remote.sample_data.ExpectedSampleData.nowPlayingMovies
import com.roland.android.data_remote.sample_data.ExpectedSampleData.popularMovies
import com.roland.android.data_remote.sample_data.ExpectedSampleData.recommendedMovies
import com.roland.android.data_remote.sample_data.ExpectedSampleData.similarMovies
import com.roland.android.data_remote.sample_data.ExpectedSampleData.topRatedMovies
import com.roland.android.data_remote.sample_data.ExpectedSampleData.trendingMovies
import com.roland.android.data_remote.sample_data.ExpectedSampleData.upcomingMovies
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteAnimeCollections
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteBollywoodMovies
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteGenreList
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteMovieDetails
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteNowPlayingMovies
import com.roland.android.data_remote.sample_data.RemoteSampleData.remotePopularMovies
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteRecommendedMovies
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteSimilarMovies
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteTopRatedMovies
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteTrendingMovies
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteUpcomingMovies
import com.roland.android.domain.entity.UseCaseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteMovieSourceImplTest {

	private val movieService = mock<MovieService>()
	private val scope = TestScope(UnconfinedTestDispatcher())
	private val remoteMovieSource = RemoteMovieSourceImpl(movieService, scope)

	@Test
	fun testFetchMovies() = runTest {
		whenever(movieService.fetchTrendingMovies()).thenReturn(remoteTrendingMovies)
		whenever(movieService.fetchPopularMovies()).thenReturn(remotePopularMovies)
		whenever(movieService.fetchNowPlayingMovies()).thenReturn(remoteNowPlayingMovies)
		whenever(movieService.fetchTopRatedMovies()).thenReturn(remoteTopRatedMovies)
		whenever(movieService.fetchUpcomingMovies()).thenReturn(remoteUpcomingMovies)
		whenever(movieService.fetchBollywoodMovies()).thenReturn(remoteBollywoodMovies)
		whenever(movieService.fetchAnimeCollection()).thenReturn(remoteAnimeCollections)
		whenever(movieService.fetchRecommendedMovies(0)).thenReturn(remoteRecommendedMovies)
		whenever(movieService.fetchSimilarMovies(2)).thenReturn(remoteSimilarMovies)
		whenever(movieService.searchMovies("")).thenReturn(remoteTrendingMovies)
		whenever(movieService.fetchMovieDetails(2)).thenReturn(remoteMovieDetails)
		whenever(movieService.fetchMovieGenres()).thenReturn(remoteGenreList)

		val trending = remoteMovieSource.fetchTrendingMovies().first()
		val popular = remoteMovieSource.fetchPopularMovies().first()
		val nowPlaying = remoteMovieSource.fetchNowPlayingMovies().first()
		val topRated = remoteMovieSource.fetchTopRatedMovies().first()
		val upcoming = remoteMovieSource.fetchUpcomingMovies().first()
		val bollywood = remoteMovieSource.fetchBollywoodMovies().first()
		val anime = remoteMovieSource.fetchAnimeCollection().first()
		val recommended = remoteMovieSource.fetchRecommendedMovies(0).first()
		val similar = remoteMovieSource.fetchSimilarMovies(2).first()
		val search = remoteMovieSource.searchMovies("").first()
		val movie = remoteMovieSource.fetchMovieDetails(2).first()
		val genres = remoteMovieSource.fetchMovieGenres().first()

		val fetchedMovieData = listOf(
			trending,
			popular,
			nowPlaying,
			topRated,
			upcoming,
			bollywood,
			anime,
			recommended,
			similar,
			search,
			movie,
			genres
		)
		val expectedMovieData = listOf(
			trendingMovies,
			popularMovies,
			nowPlayingMovies,
			topRatedMovies,
			upcomingMovies,
			bollywoodMovies,
			animeCollections,
			recommendedMovies,
			similarMovies,
			trendingMovies,
			movieDetails,
			genreList
		)
		assertEquals(fetchedMovieData, expectedMovieData)
	}

	@Test
	fun testFetchMoviesThrowsError() = runTest {
		whenever(movieService.fetchTrendingMovies()).thenThrow(RuntimeException())
		whenever(movieService.fetchPopularMovies()).thenThrow(RuntimeException())
		whenever(movieService.fetchNowPlayingMovies()).thenThrow(RuntimeException())
		whenever(movieService.fetchTopRatedMovies()).thenThrow(RuntimeException())
		whenever(movieService.fetchUpcomingMovies()).thenThrow(RuntimeException())
		whenever(movieService.fetchBollywoodMovies()).thenThrow(RuntimeException())
		whenever(movieService.fetchAnimeCollection()).thenThrow(RuntimeException())
		whenever(movieService.fetchRecommendedMovies(0)).thenThrow(RuntimeException())
		whenever(movieService.fetchSimilarMovies(2)).thenThrow(RuntimeException())
		whenever(movieService.searchMovies("")).thenThrow(RuntimeException())
		whenever(movieService.fetchMovieDetails(2)).thenThrow(RuntimeException())
		whenever(movieService.fetchMovieGenres()).thenThrow(RuntimeException())

		remoteMovieSource.fetchPopularMovies().catch {
			assertTrue(it is UseCaseException.MovieException)
		}.collect()
	}

}