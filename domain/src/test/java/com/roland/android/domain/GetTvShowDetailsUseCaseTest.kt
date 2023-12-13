package com.roland.android.domain

import com.roland.android.domain.SampleTestData.episodeDetails
import com.roland.android.domain.SampleTestData.movieCredits
import com.roland.android.domain.SampleTestData.recommendedShows
import com.roland.android.domain.SampleTestData.seasonDetails
import com.roland.android.domain.SampleTestData.showDetails
import com.roland.android.domain.SampleTestData.similarShows
import com.roland.android.domain.repository.CastRepository
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.GetSeasonDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetTvShowDetailsUseCaseTest {

	private val tvShowRepository = mock<TvShowRepository>()
	private val castRepository = mock<CastRepository>()
	private val tvShowDetailsUseCase = GetTvShowDetailsUseCase(mock(), tvShowRepository, castRepository)
	private val seasonDetailsUseCase = GetSeasonDetailsUseCase(mock(), tvShowRepository, castRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(tvShowRepository.fetchRecommendedTvShows(0)).thenReturn(flowOf(recommendedShows))
		whenever(tvShowRepository.fetchSimilarTvShows(0)).thenReturn(flowOf(similarShows))
		whenever(tvShowRepository.fetchTvShowDetails(0)).thenReturn(flowOf(showDetails))
		whenever(castRepository.fetchMovieCasts(0)).thenReturn(flowOf(movieCredits))

		val response = tvShowDetailsUseCase.process(GetTvShowDetailsUseCase.Request(0)).first()
		assertEquals(
			GetTvShowDetailsUseCase.Response(
				recommendedShows,
				similarShows,
				showDetails,
				movieCredits
			),
			response
		)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess2() = runTest {
		whenever(tvShowRepository.fetchSeasonDetails(0, 2)).thenReturn(flowOf(seasonDetails))
		whenever(tvShowRepository.fetchEpisodeDetails(0, 2, 1)).thenReturn(flowOf(episodeDetails))
		whenever(castRepository.fetchMovieCasts(0)).thenReturn(flowOf(movieCredits))

		val response = seasonDetailsUseCase.process(GetSeasonDetailsUseCase.Request(0, 2, 1)).first()
		assertEquals(
			GetSeasonDetailsUseCase.Response(
				seasonDetails,
				episodeDetails,
				movieCredits
			),
			response
		)
	}

}