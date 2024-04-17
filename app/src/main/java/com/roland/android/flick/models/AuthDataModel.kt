package com.roland.android.flick.models

import com.roland.android.domain.entity.auth_response.AccessTokenResponse
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.RequestTokenResponse
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.SessionIdResponse

data class TokenModel(
	val requestTokenResponse: RequestTokenResponse? = null,
	val accessTokenResponse: AccessTokenResponse? = null
)

data class ResponseModel(
	val sessionIdResponse: SessionIdResponse? = null,
	val response: Response? = null
)

data class AccountModel(
	val accountDetails: AccountDetails? = null,
	val accountId: String? = null
)
