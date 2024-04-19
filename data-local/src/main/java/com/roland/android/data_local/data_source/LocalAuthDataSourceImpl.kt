package com.roland.android.data_local.data_source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.roland.android.data_repository.auth.LocalAuthDataSource
import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.SessionId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")
internal val ACCOUNT_ID_KEY = stringPreferencesKey("account_id_key")
internal val USER_ID_KEY = intPreferencesKey("user_id_key")
internal val ACCOUNT_NAME_KEY = stringPreferencesKey("account_name_key")
internal val USERNAME_KEY = stringPreferencesKey("username_key")
internal val SESSION_ID_KEY = stringPreferencesKey("session_id_key")

class LocalAuthDataSourceImpl @Inject constructor(
	private val dataStore: DataStore<Preferences>
) : LocalAuthDataSource {

	override fun getAccessToken(): Flow<AccessToken> {
		return dataStore.data.map {
			AccessToken(
				accessToken = it[ACCESS_TOKEN_KEY] ?: ""
			)
		}
	}

	override suspend fun saveAccessToken(accessToken: AccessToken) {
		dataStore.edit {
			it[ACCESS_TOKEN_KEY] = accessToken.accessToken
		}
	}

	override fun getAccountId(): Flow<String> {
		return dataStore.data.map {
			it[ACCOUNT_ID_KEY] ?: ""
		}
	}

	override suspend fun saveAccountId(accountId: String) {
		dataStore.edit {
			it[ACCOUNT_ID_KEY] = accountId
		}
	}

	override fun getAccountDetails(): Flow<AccountDetails> {
		return dataStore.data.map {
			AccountDetails(
				id = it[USER_ID_KEY] ?: 0,
				name = it[ACCOUNT_NAME_KEY] ?: "",
				username = it[USERNAME_KEY] ?: "· · ·",
			)
		}
	}

	override suspend fun saveAccountDetails(accountDetails: AccountDetails) {
		dataStore.edit {
			it[USER_ID_KEY] = accountDetails.id
			it[ACCOUNT_NAME_KEY] = accountDetails.name
			it[USERNAME_KEY] = accountDetails.username
		}
	}

	override fun getSessionId(): Flow<SessionId> {
		return dataStore.data.map {
			SessionId(
				sessionId = it[SESSION_ID_KEY] ?: ""
			)
		}
	}

	override suspend fun saveSessionId(sessionId: SessionId) {
		dataStore.edit {
			it[SESSION_ID_KEY] = sessionId.sessionId
		}
	}

}