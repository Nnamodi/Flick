package com.roland.android.domain

import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.AccessTokenResponse
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.domain.entity.auth_response.RequestTokenResponse
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.SessionId
import com.roland.android.domain.entity.auth_response.SessionIdResponse
import com.roland.android.domain.repository.AuthRepository
import com.roland.android.domain.usecase.AuthRequest
import com.roland.android.domain.usecase.AuthUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AuthUseCaseTest {

	private val authRepository = mock<AuthRepository>()
	private val useCase = AuthUseCase(mock(), authRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(authRepository.generateRequestToken()).thenReturn(flowOf(RequestTokenResponse()))
		whenever(authRepository.requestAccessToken(RequestToken())).thenReturn(flowOf(AccessTokenResponse()))
		whenever(authRepository.createSession(AccessToken())).thenReturn(flowOf(SessionIdResponse()))
		whenever(authRepository.getAccountDetails("")).thenReturn(flowOf(AccountDetails()))
		whenever(authRepository.deleteSession(SessionId())).thenReturn(flowOf(SessionIdResponse()))
		whenever(authRepository.logout(AccessToken())).thenReturn(flowOf(Response()))

		val response = useCase.process(AuthUseCase.Request(authRequest = AuthRequest.GenerateRequestToken)).first()
		assertEquals(
			AuthUseCase.Response(RequestTokenResponse()),
			response
		)
	}

}