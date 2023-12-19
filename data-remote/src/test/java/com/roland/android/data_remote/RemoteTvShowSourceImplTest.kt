package com.roland.android.data_remote

import com.roland.android.data_remote.data_source.RemoteTvShowSourceImpl
import com.roland.android.data_remote.network.service.TvShowService
import com.roland.android.data_remote.sample_data.ExpectedSampleData.episodeDetails
import com.roland.android.data_remote.sample_data.ExpectedSampleData.popularShows
import com.roland.android.data_remote.sample_data.ExpectedSampleData.recommendedShows
import com.roland.android.data_remote.sample_data.ExpectedSampleData.seasonDetails
import com.roland.android.data_remote.sample_data.ExpectedSampleData.showDetails
import com.roland.android.data_remote.sample_data.ExpectedSampleData.showsAiringToday
import com.roland.android.data_remote.sample_data.ExpectedSampleData.showsSoonToAir
import com.roland.android.data_remote.sample_data.ExpectedSampleData.similarShows
import com.roland.android.data_remote.sample_data.ExpectedSampleData.topRatedShows
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteEpisodeDetails
import com.roland.android.data_remote.sample_data.RemoteSampleData.remotePopularShows
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteRecommendedShows
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteSeasonDetails
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteShowDetails
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteShowsAiringToday
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteShowsSoonToAir
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteSimilarShows
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteTopRatedShows
import com.roland.android.domain.entity.UseCaseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RemoteTvShowSourceImplTest {

	private val tvShowService = mock<TvShowService>()
	private val remoteTvShowSource = RemoteTvShowSourceImpl(tvShowService)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testFetchTvShows() = runTest {
		whenever(tvShowService.fetchTopRatedShows()).thenReturn(remoteTopRatedShows)
		whenever(tvShowService.fetchPopularShows()).thenReturn(remotePopularShows)
		whenever(tvShowService.fetchShowsAiringToday()).thenReturn(remoteShowsAiringToday)
		whenever(tvShowService.fetchShowsSoonToAir()).thenReturn(remoteShowsSoonToAir)
		whenever(tvShowService.fetchRecommendedTvShows(0)).thenReturn(remoteRecommendedShows)
		whenever(tvShowService.fetchSimilarTvShows(0)).thenReturn(remoteSimilarShows)
		whenever(tvShowService.fetchTvShowDetails(0)).thenReturn(remoteShowDetails)
		whenever(tvShowService.fetchSeasonDetails(0, 0)).thenReturn(remoteSeasonDetails)
		whenever(tvShowService.fetchEpisodeDetails(0, 0, 0)).thenReturn(remoteEpisodeDetails)

		val topRated = remoteTvShowSource.fetchTopRatedShows().first()
		val popular = remoteTvShowSource.fetchPopularShows().first()
		val airingToday = remoteTvShowSource.fetchShowsAiringToday().first()
		val soonToAir = remoteTvShowSource.fetchShowsSoonToAir().first()
		val recommended = remoteTvShowSource.fetchRecommendedTvShows(0).first()
		val similar = remoteTvShowSource.fetchSimilarTvShows(0).first()
		val show = remoteTvShowSource.fetchTvShowDetails(0).first()
		val season = remoteTvShowSource.fetchSeasonDetails(0, 0).first()
		val episode = remoteTvShowSource.fetchEpisodeDetails(0, 0, 0).first()

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
		val expectedShowData = listOf(
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
		assertEquals(fetchedShowData, expectedShowData)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testFetchTvShowsThrowsError() = runTest {
		whenever(tvShowService.fetchTopRatedShows()).thenThrow(RuntimeException())
		whenever(tvShowService.fetchPopularShows()).thenThrow(RuntimeException())
		whenever(tvShowService.fetchShowsAiringToday()).thenThrow(RuntimeException())
		whenever(tvShowService.fetchShowsSoonToAir()).thenThrow(RuntimeException())
		whenever(tvShowService.fetchRecommendedTvShows(0)).thenThrow(RuntimeException())
		whenever(tvShowService.fetchSimilarTvShows(2)).thenThrow(RuntimeException())
		whenever(tvShowService.fetchTvShowDetails(0)).thenThrow(RuntimeException())
		whenever(tvShowService.fetchSeasonDetails(0, 2)).thenThrow(RuntimeException())
		whenever(tvShowService.fetchEpisodeDetails(0, 2, 0)).thenThrow(RuntimeException())

		remoteTvShowSource.fetchPopularShows().catch {
			assertTrue(it is UseCaseException.TvShowException)
		}.collect()
	}

}