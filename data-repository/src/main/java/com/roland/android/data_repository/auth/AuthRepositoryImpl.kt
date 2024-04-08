package com.roland.android.data_repository.auth

import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.AccessTokenResponse
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.domain.entity.auth_response.RequestTokenResponse
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.SessionId
import com.roland.android.domain.entity.auth_response.SessionIdResponse
import com.roland.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
	private val authUtil: AuthUtil
) : AuthRepository {

	override fun generateRequestToken(): Flow<RequestTokenResponse> = authUtil.generateRequestToken()

	override fun requestAccessToken(requestToken: RequestToken): Flow<AccessTokenResponse> = authUtil.requestAccessToken(requestToken)

	override fun createSession(accessToken: AccessToken): Flow<SessionIdResponse> = authUtil.createSession(accessToken)

	override fun getAccountDetails(sessionId: String): Flow<AccountDetails> = authUtil.getAccountDetails(sessionId)

	override fun deleteSession(sessionId: SessionId): Flow<SessionIdResponse> = authUtil.deleteSession(sessionId)

	override fun logout(accessToken: AccessToken): Flow<Response> = authUtil.logout(accessToken)

}
