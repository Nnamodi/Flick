package com.roland.android.data_repository

import com.roland.android.data_repository.SampleTestData.episodeDetails
import com.roland.android.data_repository.SampleTestData.popularShows
import com.roland.android.data_repository.SampleTestData.recommendedShows
import com.roland.android.data_repository.SampleTestData.seasonDetails
import com.roland.android.data_repository.SampleTestData.showDetails
import com.roland.android.data_repository.SampleTestData.showsAiringToday
import com.roland.android.data_repository.SampleTestData.showsSoonToAir
import com.roland.android.data_repository.SampleTestData.similarShows
import com.roland.android.data_repository.SampleTestData.topRatedShows
import com.roland.android.data_repository.data_source.RemoteTvShowSource
import com.roland.android.data_repository.repository.TvShowRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TvShowRepositoryImplTest {

	private val remoteTvShowSource = mock<RemoteTvShowSource>()
	private val tvShowRepositoryImpl = TvShowRepositoryImpl(remoteTvShowSource)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testFetchTvShows() = runTest {
		whenever(remoteTvShowSource.fetchTopRatedShows()).thenReturn(flowOf(topRatedShows))
		whenever(remoteTvShowSource.fetchPopularShows()).thenReturn(flowOf(popularShows))
		whenever(remoteTvShowSource.fetchShowsAiringToday()).thenReturn(flowOf(showsAiringToday))
		whenever(remoteTvShowSource.fetchShowsSoonToAir()).thenReturn(flowOf(showsSoonToAir))
		whenever(remoteTvShowSource.fetchRecommendedTvShows(0)).thenReturn(flowOf(recommendedShows))
		whenever(remoteTvShowSource.fetchSimilarTvShows(0)).thenReturn(flowOf(similarShows))
		whenever(remoteTvShowSource.fetchTvShowDetails(0)).thenReturn(flowOf(showDetails))
		whenever(remoteTvShowSource.fetchSeasonDetails(0, 0)).thenReturn(flowOf(seasonDetails))
		whenever(remoteTvShowSource.fetchEpisodeDetails(0, 0, 0)).thenReturn(flowOf(episodeDetails))

		val topRated = tvShowRepositoryImpl.fetchTopRatedShows().first()
		val popular = tvShowRepositoryImpl.fetchPopularShows().first()
		val airingToday = tvShowRepositoryImpl.fetchShowsAiringToday().first()
		val soonToAir = tvShowRepositoryImpl.fetchShowsSoonToAir().first()
		val recommended = tvShowRepositoryImpl.fetchRecommendedTvShows(0).first()
		val similar = tvShowRepositoryImpl.fetchSimilarTvShows(0).first()
		val show = tvShowRepositoryImpl.fetchTvShowDetails(0).first()
		val season = tvShowRepositoryImpl.fetchSeasonDetails(0, 0).first()
		val episode = tvShowRepositoryImpl.fetchEpisodeDetails(0, 0, 0).first()

		val fetchedShowData = listOf(
			topRated,
			popular,
			airingToday,
			soonToAir,
			recommended,
			similar,
			show,
			season,
			episode
		)
		val sampleShowData = listOf(
			topRatedShows,
			popularShows,
			showsAiringToday,
			showsSoonToAir,
			recommendedShows,
			similarShows,
			showDetails,
			seasonDetails,
			episodeDetails
		)
		assertEquals(fetchedShowData, sampleShowData)
	}

}