package com.roland.android.data_remote

import com.roland.android.data_remote.data_source.RemoteCastSourceImpl
import com.roland.android.data_remote.network.service.CastService
import com.roland.android.data_remote.sample_data.ExpectedSampleData.movieCast
import com.roland.android.data_remote.sample_data.ExpectedSampleData.movieCredits
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteMovieCast
import com.roland.android.data_remote.sample_data.RemoteSampleData.remoteMovieCredits
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

class RemoteCastSourceImplTest {

	private val castService = mock<CastService>()
	private val remoteCastSource = RemoteCastSourceImpl(castService)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testFetchCastDetails() = runTest {
		whenever(castService.fetchMovieCasts(2)).thenReturn(remoteMovieCredits)
		whenever(castService.fetchCastDetails(0)).thenReturn(remoteMovieCast)

		val credits = remoteCastSource.fetchMovieCasts(2).first()
		val cast = remoteCastSource.fetchCastDetails(0).first()

		val fetchedCastData = listOf(
			credits,
			cast
		)
		val expectedCastData = listOf(
			movieCredits,
			movieCast
		)
		assertEquals(fetchedCastData, expectedCastData)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testFetchCastsThrowsError() = runTest {
		whenever(castService.fetchMovieCasts(7)).thenThrow(RuntimeException())
		whenever(castService.fetchCastDetails(0)).thenThrow(RuntimeException())

		remoteCastSource.fetchMovieCasts(0).catch {
			assertTrue(it is UseCaseException.CastException)
		}.collect()
	}

}