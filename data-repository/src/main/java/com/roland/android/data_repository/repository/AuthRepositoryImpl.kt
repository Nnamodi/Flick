package com.roland.android.data_repository.repository

import com.roland.android.data_repository.data_source.local.LocalAuthDataSource
import com.roland.android.data_repository.data_source.remote.AuthUtil
import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.AccessTokenResponse
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.domain.entity.auth_response.RequestTokenResponse
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.SessionId
import com.roland.android.domain.entity.auth_response.SessionIdResponse
import com.roland.android.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
	private val authUtil: AuthUtil,
	private val localAuthDataSource: LocalAuthDataSource,
	private val coroutineScope: CoroutineScope
) : AuthRepository {

	override fun generateRequestToken(): Flow<RequestTokenResponse> = authUtil.generateRequestToken()

	override fun requestAccessToken(requestToken: RequestToken?): Flow<AccessTokenResponse> {
		return requestToken?.let { token ->
			authUtil.requestAccessToken(token)
				.onEach {
					localAuthDataSource.saveAccessToken(AccessToken(it.accessToken))
					localAuthDataSource.saveAccountId(it.accountId)
				}
		} ?: kotlin.run {
			localAuthDataSource.getAccessToken().map {
				AccessTokenResponse(
					accessToken = it.accessToken,
					success = it.accessToken.isNotEmpty()
				)
			}
		}
	}

	override fun getAccountId(): Flow<String> = localAuthDataSource.getAccountId()

	override fun createSession(accessToken: AccessToken?): Flow<SessionIdResponse> {
		return accessToken?.let { token ->
			authUtil.createSession(token)
				.onEach {
					localAuthDataSource.saveSessionId(SessionId(it.sessionId))
				}
		} ?: kotlin.run {
			localAuthDataSource.getSessionId().map {
				SessionIdResponse(
					sessionId = it.sessionId,
					success = it.sessionId.isNotEmpty()
				)
			}
		}
	}

	override fun getAccountDetails(sessionId: String?): Flow<AccountDetails> {
		return sessionId?.let { id ->
			authUtil.getAccountDetails(id)
				.onEach {
					localAuthDataSource.saveAccountDetails(it)
				}
		} ?: kotlin.run {
			localAuthDataSource.getAccountDetails()
		}
	}

	override fun logout(): Flow<Response> {
		coroutineScope.launch {
			localAuthDataSource.apply {
				saveAccessToken(AccessToken())
				saveAccountDetails(AccountDetails())
				saveAccountId("")
				saveSessionId(SessionId(""))
			}
		}
		return flowOf(Response(statusMessage = "Logged out successfully", success = true))
	}

}
