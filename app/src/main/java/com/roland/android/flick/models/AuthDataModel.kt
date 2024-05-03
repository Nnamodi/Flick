package com.roland.android.flick.models

import androidx.paging.PagingData
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.auth_response.AccessTokenResponse
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.RequestTokenResponse
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.SessionIdResponse
import kotlinx.coroutines.flow.MutableStateFlow

val userAccountId = MutableStateFlow("")
val userAccountDetails = MutableStateFlow<AccountDetails?>(null)
val accountMediaUpdated = MutableStateFlow(false)

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

data class AccountMediaModel(
	val favoriteList: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val watchlist: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val ratedList: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val genres: List<Genre> = emptyList()
)
