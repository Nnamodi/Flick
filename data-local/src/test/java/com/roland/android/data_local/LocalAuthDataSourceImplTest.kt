package com.roland.android.data_local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import com.roland.android.data_local.data_source.ACCESS_TOKEN_KEY
import com.roland.android.data_local.data_source.ACCOUNT_NAME_KEY
import com.roland.android.data_local.data_source.LocalAuthDataSourceImpl
import com.roland.android.data_local.data_source.USERNAME_KEY
import com.roland.android.data_local.data_source.USER_ID_KEY
import com.roland.android.domain.entity.auth_response.AccessToken
import com.roland.android.domain.entity.auth_response.AccountDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LocalAuthDataSourceImplTest {

	private val dataStore = mock<DataStore<Preferences>>()
	private val authDataSource = LocalAuthDataSourceImpl(dataStore)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testGetAccessToken() = runTest {
		val token = "98416"
		val preferences = mock<Preferences>()
		whenever(preferences[ACCESS_TOKEN_KEY]).thenReturn(token)
		whenever(dataStore.data).thenReturn(flowOf(preferences))

		val result = authDataSource.getAccessToken().first()
		assertEquals(AccessToken(token), result)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testSaveAccessToken() = runTest {
		val token = "54698"
		val preferences = mock<MutablePreferences>()
		whenever(preferences.toMutablePreferences()).thenReturn(preferences)
		whenever(dataStore.updateData(any())).thenAnswer {
			runBlocking {
				it.getArgument<suspend (Preferences) -> Preferences>(0).invoke(preferences)
			}
			preferences
		}

		authDataSource.saveAccessToken(AccessToken(token))
		verify(preferences) [ACCESS_TOKEN_KEY] = token
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testGetAccountDetails() = runTest {
		val id = 516
		val name = "Roland"
		val username = "nnamodi"
		val accountDetails = AccountDetails(id = id, name = name, username = username)
		val preferences = mock<Preferences>()
		whenever(preferences[USER_ID_KEY]).thenReturn(id)
		whenever(preferences[ACCOUNT_NAME_KEY]).thenReturn(name)
		whenever(preferences[USERNAME_KEY]).thenReturn(username)
		whenever(dataStore.data).thenReturn(flowOf(preferences))

		val result = authDataSource.getAccountDetails().first()
		assertEquals(accountDetails, result)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testSaveAccountDetails() = runTest {
		val id = 516
		val name = "Roland"
		val username = "nnamodi"
		val accountDetails = AccountDetails(id = id, name = name, username = username)
		val preferences = mock<MutablePreferences>()
		whenever(preferences.toMutablePreferences()).thenReturn(preferences)
		whenever(dataStore.updateData(any())).thenAnswer {
			runBlocking {
				it.getArgument<suspend (Preferences) -> Preferences>(0).invoke(preferences)
			}
			preferences
		}

		authDataSource.saveAccountDetails(accountDetails)
		verify(preferences) [USER_ID_KEY] = id
		verify(preferences) [ACCOUNT_NAME_KEY] = name
		verify(preferences) [USERNAME_KEY] = username
	}

}