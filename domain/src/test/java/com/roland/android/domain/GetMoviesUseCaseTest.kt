package com.roland.android.domain

import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class GetMoviesUseCaseTest {

	private val movieRepository = mock<MovieRepository>()
	private val useCase = GetMoviesUseCase(mock(), movieRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		val movie1 = Movie(id = 0, title = "Oppenhiemer")
		val movie2 = Movie(id = 1, title = "Barbie")
		val movie3 = Movie(id = 2, title = "The Burial")
		val movie4 = Movie(id = 3, title = "Infinite")

		val trendingMovies = MovieList(results = listOf(movie1, movie2), totalResults = 2)
		val popularMovies = MovieList(results = listOf(movie1, movie2, movie3, movie4), totalResults = 4)
		val nowPlayingMovies = MovieList(results = listOf(movie1, movie2, movie4), totalResults = 3)
		val topRatedMovies = MovieList(results = listOf(movie1, movie4), totalResults = 2)
		val upcomingMovies = MovieList(results = listOf(movie3), totalResults = 1)

		whenever(movieRepository.fetchTrendingMovies()).thenReturn(flowOf(trendingMovies))
		whenever(movieRepository.fetchPopularMovies()).thenReturn(flowOf(popularMovies))
		whenever(movieRepository.fetchNowPlayingMovies()).thenReturn(flowOf(nowPlayingMovies))
		whenever(movieRepository.fetchTopRatedMovies()).thenReturn(flowOf(topRatedMovies))
		whenever(movieRepository.fetchUpcomingMovies()).thenReturn(flowOf(upcomingMovies))

		val response = useCase.process(GetMoviesUseCase.Request).first()
		assertEquals(
			GetMoviesUseCase.Response(
				trendingMovies,
				popularMovies,
				nowPlayingMovies,
				topRatedMovies,
				upcomingMovies
			),
			response
		)
	}

}