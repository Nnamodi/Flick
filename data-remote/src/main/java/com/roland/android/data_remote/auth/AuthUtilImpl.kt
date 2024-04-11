package com.roland.android.data_remote.auth

import com.roland.android.data_remote.utils.Converters.convertFromAccessToken
import com.roland.android.data_remote.utils.Converters.convertFromRequestToken
import com.roland.android.data_remote.utils.Converters.convertFromSessionId
import com.roland.android.data_remote.utils.Converters.convertToAccessTokenResponse
import com.roland.android.data_remote.utils.Converters.convertToAccountDetails
import com.roland.android.data_remote.utils.Converters.convertToRequestTokenResponse
import com.roland.android.data_remote.utils.Converters.convertToResponse
import com.roland.android.data_remote.utils.Converters.convertToSessionIdResponse
import com.roland.android.data_repository.auth.AuthUtil
import com.roland.android.domain.entity.UseCaseException
import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.AccessTokenResponse
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.domain.entity.auth_response.RequestTokenResponse
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.SessionId
import com.roland.android.domain.entity.auth_response.SessionIdResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthUtilImpl @Inject constructor(
	private val authService: AuthService
) : AuthUtil {

	override fun generateRequestToken(): Flow<RequestTokenResponse> = flow {
		emit(authService.generateRequestToken())
	}.map { responseModel ->
		convertToRequestTokenResponse(responseModel)
	}.catch {
		throw UseCaseException.AuthException(it)
	}

	override fun requestAccessToken(requestToken: RequestToken): Flow<AccessTokenResponse> = flow {
		val requestTokenModel = convertFromRequestToken(requestToken)
		emit(authService.requestAccessToken(requestTokenModel))
	}.map { responseModel ->
		convertToAccessTokenResponse(responseModel)
	}.catch {
		throw UseCaseException.AuthException(it)
	}

	override fun createSession(accessToken: AccessToken): Flow<SessionIdResponse> = flow {
		val accessTokenModel = convertFromAccessToken(accessToken)
		emit(authService.createSession(accessTokenModel))
	}.map { responseModel ->
		convertToSessionIdResponse(responseModel)
	}.catch {
		throw UseCaseException.AuthException(it)
	}

	override fun getAccountDetails(sessionId: String): Flow<AccountDetails> = flow {
		emit(authService.getAccountDetails(sessionId))
	}.map { responseModel ->
		convertToAccountDetails(responseModel)
	}.catch {
		throw UseCaseException.AuthException(it)
	}

	override fun deleteSession(sessionId: SessionId): Flow<SessionIdResponse> = flow {
		val sessionIdModel = convertFromSessionId(sessionId)
		emit(authService.deleteSession(sessionIdModel))
	}.map { responseModel ->
		convertToSessionIdResponse(responseModel)
	}.catch {
		throw UseCaseException.AuthException(it)
	}

	override fun logout(accessToken: AccessToken): Flow<Response> = flow {
		val accessTokenModel = convertFromAccessToken(accessToken)
		emit(authService.logout(accessTokenModel))
	}.map { responseModel ->
		convertToResponse(responseModel)
	}.catch {
		throw UseCaseException.AuthException(it)
	}

}
