package com.roland.android.data_repository.auth

import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.SessionId
import kotlinx.coroutines.flow.Flow

interface LocalAuthDataSource {

	fun getAccessToken(): Flow<AccessToken>

	suspend fun saveAccessToken(accessToken: AccessToken)

	fun getAccountId(): Flow<String>

	suspend fun saveAccountId(accountId: String)

	fun getAccountDetails(): Flow<AccountDetails>

	suspend fun saveAccountDetails(accountDetails: AccountDetails)

	fun getSessionId(): Flow<SessionId>

	suspend fun saveSessionId(sessionId: SessionId)

}