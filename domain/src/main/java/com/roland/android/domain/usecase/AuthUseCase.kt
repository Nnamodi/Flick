package com.roland.android.domain.usecase

import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.AccessTokenResponse
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.domain.entity.auth_response.RequestTokenResponse
import com.roland.android.domain.entity.auth_response.SessionId
import com.roland.android.domain.entity.auth_response.SessionIdResponse
import com.roland.android.domain.repository.AuthRepository
import com.roland.android.domain.usecase.AuthRequest.CreateSession
import com.roland.android.domain.usecase.AuthRequest.GenerateRequestToken
import com.roland.android.domain.usecase.AuthRequest.GetAccountDetails
import com.roland.android.domain.usecase.AuthRequest.Logout
import com.roland.android.domain.usecase.AuthRequest.RequestAccessToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthUseCase @Inject constructor(
	configuration: Configuration,
	private val authRepository: AuthRepository
) : UseCase<AuthUseCase.Request, AuthUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> {
		return when (request.authRequest) {
			GenerateRequestToken -> {
				authRepository.generateRequestToken().map { Response(requestTokenResponse = it) }
			}
			RequestAccessToken -> {
				authRepository.requestAccessToken(request.requestToken).map { Response(accessTokenResponse = it) }
			}
			CreateSession -> {
				authRepository.createSession(request.accessToken).map { Response(sessionIdResponse = it) }
			}
			GetAccountDetails -> {
				combine(
					authRepository.getAccountDetails(request.sessionIdString),
					authRepository.getAccountId()
				) { accountDetails, accountId ->
					Response(accountDetails = accountDetails, accountId = accountId)
				}
			}
			Logout -> {
				authRepository.logout().map { Response(response = it) }
			}
		}
	}

	data class Request(
		val requestToken: RequestToken? = null,
		val accessToken: AccessToken? = null,
		val sessionIdString: String? = null,
		val sessionId: SessionId? = null,
		val authRequest: AuthRequest
	) : UseCase.Request

	data class Response(
		val requestTokenResponse: RequestTokenResponse? = null,
		val accessTokenResponse: AccessTokenResponse? = null,
		val sessionIdResponse: SessionIdResponse? = null,
		val accountDetails: AccountDetails? = null,
		val accountId: String? = null,
		val response: com.roland.android.domain.entity.auth_response.Response? = null
	) : UseCase.Response

}

enum class AuthRequest {
	GenerateRequestToken,
	RequestAccessToken,
	CreateSession,
	GetAccountDetails,
	Logout
}
