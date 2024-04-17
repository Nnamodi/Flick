package com.roland.android.flick.ui.screens.account

import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.domain.entity.auth_response.SessionId

sealed class AuthActions {

	object GenerateRequest : AuthActions()

	data class RequestAccessToken(val requestToken: RequestToken) : AuthActions()

	data class Logout(
		val sessionId: SessionId,
		val accessToken: AccessToken
	) : AuthActions()

}
