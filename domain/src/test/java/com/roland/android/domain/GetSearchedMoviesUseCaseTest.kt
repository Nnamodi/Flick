package com.roland.android.domain

import com.roland.android.domain.SampleTestData.genreList
import com.roland.android.domain.SampleTestData.trendingMovies
import com.roland.android.domain.SampleTestData.trendingShows
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.GetSearchedMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class GetSearchedMoviesUseCaseTest {

	private val movieRepository = mock<MovieRepository>()
	private val tvShowRepository = mock<TvShowRepository>()
	private val useCase = GetSearchedMoviesUseCase(mock(), movieRepository, tvShowRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testSearchProcess() = runTest {
		whenever(movieRepository.searchMoviesAndShows("")).thenReturn(flowOf(trendingMovies))
		whenever(movieRepository.searchMovies("")).thenReturn(flowOf(trendingMovies))
		whenever(tvShowRepository.searchTvShows("")).thenReturn(flowOf(trendingShows))
		whenever(movieRepository.fetchMovieGenres()).thenReturn(flowOf(genreList))
		whenever(tvShowRepository.fetchTvShowGenres()).thenReturn(flowOf(genreList))

		val response = useCase.process(GetSearchedMoviesUseCase.Request("")).first()
		assertEquals(
			GetSearchedMoviesUseCase.Response(
				trendingMovies,
				trendingMovies,
				trendingShows,
				genreList,
				genreList
			),
			response
		)
	}

}