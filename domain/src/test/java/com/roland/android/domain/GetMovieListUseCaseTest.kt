package com.roland.android.domain

import com.roland.android.domain.SampleTestData.animeCollections
import com.roland.android.domain.SampleTestData.animeShows
import com.roland.android.domain.SampleTestData.bollywoodMovies
import com.roland.android.domain.SampleTestData.bollywoodShows
import com.roland.android.domain.SampleTestData.genreList
import com.roland.android.domain.SampleTestData.nowPlayingMovies
import com.roland.android.domain.SampleTestData.popularMovies
import com.roland.android.domain.SampleTestData.popularShows
import com.roland.android.domain.SampleTestData.showsAiringToday
import com.roland.android.domain.SampleTestData.topRatedMovies
import com.roland.android.domain.SampleTestData.topRatedShows
import com.roland.android.domain.SampleTestData.trendingMovies
import com.roland.android.domain.SampleTestData.trendingShows
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.Category
import com.roland.android.domain.usecase.GetMovieListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class GetMovieListUseCaseTest {

	private val movieRepository = mock<MovieRepository>()
	private val tvShowRepository = mock<TvShowRepository>()
	private val useCase = GetMovieListUseCase(mock(), movieRepository, tvShowRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(movieRepository.fetchTrendingMovies()).thenReturn(flowOf(trendingMovies))
		whenever(movieRepository.fetchNowPlayingMovies()).thenReturn(flowOf(nowPlayingMovies))
		whenever(movieRepository.fetchTopRatedMovies()).thenReturn(flowOf(topRatedMovies))
		whenever(movieRepository.fetchAnimeCollection()).thenReturn(flowOf(bollywoodMovies))
		whenever(movieRepository.fetchBollywoodMovies()).thenReturn(flowOf(animeCollections))
		whenever(movieRepository.fetchPopularMovies()).thenReturn(flowOf(popularMovies))
		whenever(movieRepository.fetchMovieGenres()).thenReturn(flowOf(genreList))
		whenever(tvShowRepository.fetchTrendingShows()).thenReturn(flowOf(trendingShows))
		whenever(tvShowRepository.fetchShowsAiringToday()).thenReturn(flowOf(showsAiringToday))
		whenever(tvShowRepository.fetchTopRatedShows()).thenReturn(flowOf(topRatedShows))
		whenever(tvShowRepository.fetchAnimeShows()).thenReturn(flowOf(animeShows))
		whenever(tvShowRepository.fetchBollywoodShows()).thenReturn(flowOf(bollywoodShows))
		whenever(tvShowRepository.fetchPopularShows()).thenReturn(flowOf(popularShows))
		whenever(tvShowRepository.fetchTvShowGenres()).thenReturn(flowOf(genreList))

		val response = useCase.process(GetMovieListUseCase.Request(Category.POPULAR_MOVIES)).first()
		assertEquals(
			GetMovieListUseCase.Response(
				result = popularMovies,
				movieGenre = genreList,
				seriesGenre = genreList
			),
			response
		)
	}

}