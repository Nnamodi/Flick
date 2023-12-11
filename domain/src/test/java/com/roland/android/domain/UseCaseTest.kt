package com.roland.android.domain

import com.roland.android.domain.entity.Result
import com.roland.android.domain.usecase.UseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class UseCaseTest {

	@OptIn(ExperimentalCoroutinesApi::class)
	private val configuration = UseCase.Configuration(StandardTestDispatcher())
	private val request = mock<UseCase.Request>()
	private val response = mock<UseCase.Response>()

	private lateinit var useCase: UseCase<UseCase.Request, UseCase.Response>

	@Before
	fun setUp() {
		useCase = object : UseCase<UseCase.Request, UseCase.Response>(configuration) {
			override fun process(request: Request): Flow<Response> {
				assertEquals(this@UseCaseTest.request, request)
				return flowOf(response)
			}

		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testExecuteSuccess() = runTest {
		val result = useCase.execute(request).first()
		assertEquals(Result.Success(response), result)
	}

}
