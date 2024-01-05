package com.roland.android.domain

import com.roland.android.domain.SampleTestData.animeShows
import com.roland.android.domain.SampleTestData.bollywoodShows
import com.roland.android.domain.SampleTestData.genreList
import com.roland.android.domain.SampleTestData.popularShows
import com.roland.android.domain.SampleTestData.showsAiringToday
import com.roland.android.domain.SampleTestData.showsSoonToAir
import com.roland.android.domain.SampleTestData.topRatedShows
import com.roland.android.domain.SampleTestData.trendingShows
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.GetFurtherTvShowUseCase
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
	private val furtherTvShowUseCase = GetFurtherTvShowUseCase(mock(), tvShowRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(tvShowRepository.fetchTrendingShows()).thenReturn(flowOf(trendingShows))
		whenever(tvShowRepository.fetchPopularShows()).thenReturn(flowOf(popularShows))
		whenever(tvShowRepository.fetchShowsAiringToday()).thenReturn(flowOf(showsAiringToday))
		whenever(tvShowRepository.fetchTopRatedShows()).thenReturn(flowOf(topRatedShows))
		whenever(tvShowRepository.fetchShowsSoonToAir()).thenReturn(flowOf(showsSoonToAir))

		val response = tvShowUseCase.process(GetTvShowUseCase.Request).first()
		assertEquals(
			GetTvShowUseCase.Response(
				trendingShows,
				popularShows,
				showsAiringToday,
				topRatedShows,
				showsSoonToAir
			),
			response
		)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess2() = runTest {
		whenever(tvShowRepository.fetchBollywoodShows()).thenReturn(flowOf(bollywoodShows))
		whenever(tvShowRepository.fetchAnimeShows()).thenReturn(flowOf(animeShows))
		whenever(tvShowRepository.fetchTvShowGenres()).thenReturn(flowOf(genreList))

		val response = furtherTvShowUseCase.process(GetFurtherTvShowUseCase.Request).first()
		assertEquals(
			GetFurtherTvShowUseCase.Response(
				bollywoodShows,
				animeShows,
				genreList
			),
			response
		)
	}

}