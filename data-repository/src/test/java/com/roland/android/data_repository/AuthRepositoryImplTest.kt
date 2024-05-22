package com.roland.android.data_repository

import com.roland.android.data_repository.data_source.local.LocalAuthDataSource
import com.roland.android.data_repository.data_source.remote.AuthUtil
import com.roland.android.data_repository.repository.AuthRepositoryImpl
import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.AccessTokenResponse
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.domain.entity.auth_response.RequestTokenResponse
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.SessionId
import com.roland.android.domain.entity.auth_response.SessionIdResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AuthRepositoryImplTest {

	private val authUtil = mock<AuthUtil>()
	private val localAuthSource = mock<LocalAuthDataSource>()
	private val authRepositoryImpl = AuthRepositoryImpl(authUtil, localAuthSource)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testAuthProcess() = runTest {
		whenever(authUtil.generateRequestToken()).thenReturn(flowOf(RequestTokenResponse()))
		whenever(authUtil.requestAccessToken(RequestToken())).thenReturn(flowOf(AccessTokenResponse()))
		whenever(authUtil.createSession(AccessToken())).thenReturn(flowOf(SessionIdResponse()))
		whenever(authUtil.getAccountDetails("")).thenReturn(flowOf(AccountDetails()))
		whenever(authUtil.deleteSession(SessionId())).thenReturn(flowOf(SessionIdResponse()))
		whenever(authUtil.logout(AccessToken())).thenReturn(flowOf(Response()))

		val requestToken = authRepositoryImpl.generateRequestToken().first()
		val accessToken = authRepositoryImpl.requestAccessToken(RequestToken()).first()
		val session = authRepositoryImpl.createSession(AccessToken()).first()
		val accountDetails = authRepositoryImpl.getAccountDetails("").first()
		val sessionId = authRepositoryImpl.deleteSession(SessionId()).first()
		val response = authRepositoryImpl.logout(AccessToken()).first()

		val authData = listOf(
			requestToken,
			accessToken,
			session,
			accountDetails,
			sessionId,
			response
		)
		val sampleAuthData = listOf(
			RequestTokenResponse(),
			AccessTokenResponse(),
			SessionIdResponse(),
			AccountDetails(),
			SessionIdResponse(),
			Response()
		)
		assertEquals(authData, sampleAuthData)
	}

}