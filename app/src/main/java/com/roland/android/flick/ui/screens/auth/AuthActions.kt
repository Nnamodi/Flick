package com.roland.android.flick.ui.screens.auth

import com.roland.android.domain.entity.auth_response.RequestToken

sealed class AuthActions {

	object GenerateRequest : AuthActions()

	object AuthorizationCancelled : AuthActions()

	data class RequestAccessToken(val requestToken: RequestToken) : AuthActions()

}
