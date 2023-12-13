package com.roland.android.domain

import com.roland.android.domain.SampleTestData.movieCast
import com.roland.android.domain.repository.CastRepository
import com.roland.android.domain.usecase.GetCastDetailsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetCastDetailsUseCaseTest {

	private val castRepository = mock<CastRepository>()
	private val useCase = GetCastDetailsUseCase(mock(), castRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(castRepository.fetchCastDetails(0)).thenReturn(flowOf(movieCast))

		val response = useCase.process(GetCastDetailsUseCase.Request(0)).first()
		assertEquals(
			GetCastDetailsUseCase.Response(movieCast),
			response
		)
	}

}