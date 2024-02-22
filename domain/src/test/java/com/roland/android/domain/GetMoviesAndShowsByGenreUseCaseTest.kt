package com.roland.android.domain

import com.roland.android.domain.SampleTestData.genreList
import com.roland.android.domain.SampleTestData.trendingMovies
import com.roland.android.domain.SampleTestData.trendingShows
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.Collection.MOVIES
import com.roland.android.domain.usecase.GetMoviesAndShowByGenreUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class GetMoviesAndShowsByGenreUseCaseTest {

	private val movieRepository = mock<MovieRepository>()
	private val tvShowRepository = mock<TvShowRepository>()
	private val useCase = GetMoviesAndShowByGenreUseCase(mock(), movieRepository, tvShowRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(movieRepository.fetchMoviesByGenre("")).thenReturn(flowOf(trendingMovies))
		whenever(tvShowRepository.fetchShowsByGenre("")).thenReturn(flowOf(trendingShows))
		whenever(movieRepository.fetchMovieGenres()).thenReturn(flowOf(genreList))
		whenever(tvShowRepository.fetchTvShowGenres()).thenReturn(flowOf(genreList))

		val response = useCase.process(
			GetMoviesAndShowByGenreUseCase.Request("", MOVIES)
		).first()
		assertEquals(
			GetMoviesAndShowByGenreUseCase.Response(
				movieList = trendingMovies,
				movieGenres = genreList,
				seriesGenres = genreList
			),
			response
		)
	}

}