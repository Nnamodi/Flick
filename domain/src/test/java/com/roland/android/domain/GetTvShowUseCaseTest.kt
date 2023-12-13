package com.roland.android.domain

import com.roland.android.domain.SampleTestData.popularShows
import com.roland.android.domain.SampleTestData.genreList
import com.roland.android.domain.SampleTestData.showsAiringToday
import com.roland.android.domain.SampleTestData.showsSoonToAir
import com.roland.android.domain.SampleTestData.topRatedShows
import com.roland.android.domain.repository.TvShowRepository
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
	private val useCase = GetTvShowUseCase(mock(), tvShowRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(tvShowRepository.fetchTopRatedShows()).thenReturn(flowOf(topRatedShows))
		whenever(tvShowRepository.fetchPopularShows()).thenReturn(flowOf(popularShows))
		whenever(tvShowRepository.fetchShowsAiringToday()).thenReturn(flowOf(showsAiringToday))
		whenever(tvShowRepository.fetchShowsSoonToAir()).thenReturn(flowOf(showsSoonToAir))
		whenever(tvShowRepository.fetchTvShowGenres()).thenReturn(flowOf(genreList))

		val response = useCase.process(GetTvShowUseCase.Request).first()
		assertEquals(
			GetTvShowUseCase.Response(
				topRatedShows,
				popularShows,
				showsAiringToday,
				showsSoonToAir,
				genreList
			),
			response
		)
	}

}