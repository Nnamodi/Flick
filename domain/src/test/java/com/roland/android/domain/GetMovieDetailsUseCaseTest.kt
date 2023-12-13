package com.roland.android.domain

import com.roland.android.domain.SampleTestData.movieCredits
import com.roland.android.domain.SampleTestData.movieDetails
import com.roland.android.domain.SampleTestData.recommendedMovies
import com.roland.android.domain.SampleTestData.similarMovies
import com.roland.android.domain.repository.CastRepository
import com.roland.android.domain.repository.MovieRepository
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
	private val castRepository = mock<CastRepository>()
	private val useCase = GetMovieDetailsUseCase(mock(), movieRepository, castRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(movieRepository.fetchRecommendedMovies(2)).thenReturn(flowOf(recommendedMovies))
		whenever(movieRepository.fetchSimilarMovies(2)).thenReturn(flowOf(similarMovies))
		whenever(movieRepository.fetchMovieDetails(2)).thenReturn(flowOf(movieDetails))
		whenever(castRepository.fetchMovieCasts(2)).thenReturn(flowOf(movieCredits))

		val response = useCase.process(GetMovieDetailsUseCase.Request(2)).first()
		assertEquals(
			GetMovieDetailsUseCase.Response(
				recommendedMovies,
				similarMovies,
				movieDetails,
				movieCredits
			),
			response
		)
	}

}