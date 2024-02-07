package com.roland.android.domain

import com.roland.android.domain.SampleTestData.genreList
import com.roland.android.domain.SampleTestData.movieDetails
import com.roland.android.domain.SampleTestData.recommendedMovies
import com.roland.android.domain.SampleTestData.similarMovies
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class GetMovieDetailsUseCaseTest {

	private val movieRepository = mock<MovieRepository>()
	private val tvShowRepository = mock<TvShowRepository>()
	private val useCase = GetMovieDetailsUseCase(mock(), movieRepository, tvShowRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(movieRepository.fetchRecommendedMovies(2)).thenReturn(flowOf(recommendedMovies))
		whenever(movieRepository.fetchSimilarMovies(2)).thenReturn(flowOf(similarMovies))
		whenever(movieRepository.fetchMovieDetails(2)).thenReturn(flowOf(movieDetails))
		whenever(movieRepository.fetchMovieGenres()).thenReturn(flowOf(genreList))
		whenever(tvShowRepository.fetchTvShowGenres()).thenReturn(flowOf(genreList))

		val response = useCase.process(GetMovieDetailsUseCase.Request(2)).first()
		assertEquals(
			GetMovieDetailsUseCase.Response(
				movieDetails,
				recommendedMovies,
				similarMovies,
				genreList,
				genreList
			),
			response
		)
	}

}