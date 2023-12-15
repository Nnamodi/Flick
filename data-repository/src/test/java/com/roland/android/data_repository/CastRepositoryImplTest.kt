package com.roland.android.data_repository

import com.roland.android.data_repository.SampleTestData.movieCast
import com.roland.android.data_repository.SampleTestData.movieCredits
import com.roland.android.data_repository.data_source.RemoteCastSource
import com.roland.android.data_repository.repository.CastRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CastRepositoryImplTest {

	private val remoteCastSource = mock<RemoteCastSource>()
	private val castRepositoryImpl = CastRepositoryImpl(remoteCastSource)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testFetchCastDetails() = runTest {
		whenever(remoteCastSource.fetchMovieCasts(2)).thenReturn(flowOf(movieCredits))
		whenever(remoteCastSource.fetchCastDetails(0)).thenReturn(flowOf(movieCast))

		val credits = castRepositoryImpl.fetchMovieCasts(2).first()
		val cast = castRepositoryImpl.fetchCastDetails(0).first()

		val fetchedCastData = listOf(
			credits,
			cast
		)
		val sampleCastData = listOf(
			movieCredits,
			movieCast
		)
		assertEquals(fetchedCastData, sampleCastData)
	}

}