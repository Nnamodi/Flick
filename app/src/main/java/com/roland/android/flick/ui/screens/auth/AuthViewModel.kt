package com.roland.android.flick.ui.screens.auth

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.domain.entity.auth_response.SessionId
import com.roland.android.domain.usecase.AuthRequest
import com.roland.android.domain.usecase.AuthUseCase
import com.roland.android.flick.models.TokenModel
import com.roland.android.flick.models.userAccountDetails
import com.roland.android.flick.models.userAccountId
import com.roland.android.flick.state.AuthUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
	private val authUseCase: AuthUseCase,
	private val converter: ResponseConverter
) : ViewModel() {

	private val _authUiState = MutableStateFlow(AuthUiState())
	var authUiState by mutableStateOf(_authUiState.value); private set

	init {
		requestAccessToken(null)

		viewModelScope.launch {
			_authUiState.collectLatest {
				authUiState = it
			}
		}
	}

	fun onNewIntent(intentData: Uri?) {
		_authUiState.update { it.copy(intentData = intentData) }
	}

	fun onActivityResumed(resumed: Boolean) {
		_authUiState.update { it.copy(activityResumed = resumed) }
	}

	fun authActions(action: AuthActions) {
		when (action) {
			AuthActions.GenerateRequest -> generateRequest()
			AuthActions.AuthorizationCancelled -> tokenAuthorizationCancelled()
			is AuthActions.RequestAccessToken -> requestAccessToken(action.requestToken)
			is AuthActions.Logout -> logout(action.sessionId, action.accessToken)
		}
	}

	private fun generateRequest() {
		_authUiState.update { it.copy(tokenData = null) }
		viewModelScope.launch {
			authUseCase.execute(
				AuthUseCase.Request(authRequest = AuthRequest.GenerateRequestToken)
			)
				.map { converter.convertTokenData(it) }
				.collect { data ->
					_authUiState.update { it.copy(tokenData = data, activityResumed = false) }
				}
		}
	}

	private fun tokenAuthorizationCancelled() {
		_authUiState.update { it.copy(tokenData = State.Success(TokenModel())) }
	}

	private fun requestAccessToken(requestToken: RequestToken?) {
		_authUiState.update { it.copy(intentData = null) }
		viewModelScope.launch {
			authUseCase.execute(
				AuthUseCase.Request(
					requestToken = requestToken,
					authRequest = AuthRequest.RequestAccessToken
				)
			)
				.map { converter.convertTokenData(it) }
				.collect { data ->
					_authUiState.update { it.copy(tokenData = data) }
					if (data !is State.Success) return@collect
					val token = data.data.accessTokenResponse?.accessToken ?: return@collect
					val accessToken = AccessToken(token)
						.takeIf { requestToken != null }
					createSession(accessToken)
				}
		}
	}

	private fun createSession(accessToken: AccessToken?) {
		viewModelScope.launch {
			authUseCase.execute(
				AuthUseCase.Request(
					accessToken = accessToken,
					authRequest = AuthRequest.CreateSession
				)
			)
				.map { converter.convertResponseData(it) }
				.collect { data ->
					if (data !is State.Success) return@collect
					val sessionId = data.data.sessionIdResponse?.sessionId
						.takeIf { accessToken != null }
					getAccountDetails(sessionId)
				}
		}
	}

	private fun getAccountDetails(sessionId: String?) {
		viewModelScope.launch {
			authUseCase.execute(
				AuthUseCase.Request(
					sessionIdString = sessionId,
					authRequest = AuthRequest.GetAccountDetails
				)
			)
				.map { converter.convertAccountData(it) }
				.collect { data ->
					if (data !is State.Success) return@collect
					if (data.data.accountId == null) return@collect
					userAccountId.value = data.data.accountId
					userAccountDetails.value = data.data.accountDetails
				}
		}
	}

	private fun logout(sessionId: SessionId, accessToken: AccessToken) {
		viewModelScope.launch {
			authUseCase.execute(
				AuthUseCase.Request(
					sessionId = sessionId,
					accessToken = accessToken,
					authRequest = AuthRequest.Logout
				)
			)
				.map { converter.convertResponseData(it) }
				.collect { data ->
					_authUiState.update { it.copy(responseData = data) }
				}
		}
	}

}