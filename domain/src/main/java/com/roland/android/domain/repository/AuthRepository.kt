package com.roland.android.domain.repository

import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.AccessTokenResponse
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.domain.entity.auth_response.RequestTokenResponse
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.SessionId
import com.roland.android.domain.entity.auth_response.SessionIdResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

	fun generateRequestToken(): Flow<RequestTokenResponse>

	fun requestAccessToken(requestToken: RequestToken): Flow<AccessTokenResponse>

	fun createSession(accessToken: AccessToken): Flow<SessionIdResponse>

	fun getAccountDetails(sessionId: String): Flow<AccountDetails>

	fun deleteSession(sessionId: SessionId): Flow<SessionIdResponse>

	fun logout(accessToken: AccessToken): Flow<Response>

}
