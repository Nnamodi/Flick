package com.roland.android.domain

import com.roland.android.domain.SampleTestData.bollywoodShows
import com.roland.android.domain.SampleTestData.genreList
import com.roland.android.domain.SampleTestData.koreanShows
import com.roland.android.domain.SampleTestData.nigerianShows
import com.roland.android.domain.SampleTestData.popularShows
import com.roland.android.domain.SampleTestData.showsAiringToday
import com.roland.android.domain.SampleTestData.topRatedShows
import com.roland.android.domain.SampleTestData.trendingShows
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.GetTvShowByRegionUseCase
import com.roland.android.domain.usecase.GetTvShowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class GetTvShowUseCaseTest {

	private val tvShowRepository = mock<TvShowRepository>()
	private val tvShowUseCase = GetTvShowUseCase(mock(), tvShowRepository)
	private val tvShowByRegionUseCase = GetTvShowByRegionUseCase(mock(), tvShowRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(tvShowRepository.fetchTrendingShows()).thenReturn(flowOf(trendingShows))
		whenever(tvShowRepository.fetchPopularShows()).thenReturn(flowOf(popularShows))
		whenever(tvShowRepository.fetchShowsAiringToday()).thenReturn(flowOf(showsAiringToday))
		whenever(tvShowRepository.fetchTopRatedShows()).thenReturn(flowOf(topRatedShows))
		whenever(tvShowRepository.fetchTvShowGenres()).thenReturn(flowOf(genreList))

		val response = tvShowUseCase.process(GetTvShowUseCase.Request).first()
		assertEquals(
			GetTvShowUseCase.Response(
				trendingShows,
				popularShows,
				showsAiringToday,
				topRatedShows,
				genreList
			),
			response
		)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess2() = runTest {
		whenever(tvShowRepository.fetchShowsByRegion("NG")).thenReturn(flowOf(nigerianShows))
		whenever(tvShowRepository.fetchShowsByRegion("KP")).thenReturn(flowOf(koreanShows))
		whenever(tvShowRepository.fetchShowsByRegion("IN")).thenReturn(flowOf(bollywoodShows))

		val response = tvShowByRegionUseCase.process(GetTvShowByRegionUseCase.Request).first()
		assertEquals(
			GetTvShowByRegionUseCase.Response(
				nigerianShows, koreanShows, bollywoodShows
			),
			response
		)
	}

}