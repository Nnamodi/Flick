package com.roland.android.domain

import com.roland.android.domain.SampleTestData.genreList
import com.roland.android.domain.SampleTestData.showsSoonToAir
import com.roland.android.domain.SampleTestData.upcomingMovies
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.GetUpcomingMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class GetUpcomingMoviesUseCaseTest {

	private val movieRepository = mock<MovieRepository>()
	private val tvShowRepository = mock<TvShowRepository>()
	private val useCase = GetUpcomingMoviesUseCase(mock(), movieRepository, tvShowRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(movieRepository.fetchUpcomingMovies()).thenReturn(flowOf(upcomingMovies))
		whenever(tvShowRepository.fetchShowsSoonToAir()).thenReturn(flowOf(showsSoonToAir))
		whenever(movieRepository.fetchMovieGenres()).thenReturn(flowOf(genreList))
		whenever(tvShowRepository.fetchTvShowGenres()).thenReturn(flowOf(genreList))

		val response = useCase.process(GetUpcomingMoviesUseCase.Request).first()
		assertEquals(
			GetUpcomingMoviesUseCase.Response(
				upcomingMovies = upcomingMovies,
				upcomingShows = showsSoonToAir,
				movieGenres = genreList,
				seriesGenres = genreList
			),
			response
		)
	}

}