package com.roland.android.flick.models

import androidx.paging.PagingData
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.auth_response.AccessTokenResponse
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.entity.auth_response.RequestTokenResponse
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.SessionIdResponse
import com.roland.android.domain.usecase.MediaCategory
import kotlinx.coroutines.flow.MutableStateFlow

val accountSessionId = MutableStateFlow<String?>(null)
val userAccountId = MutableStateFlow("")
val userAccountDetails = MutableStateFlow<AccountDetails?>(null)
val updatedMediaCategory = MutableStateFlow<MediaCategory?>(null)

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

data class WatchlistedMediaModel(
	val movies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val shows: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movieGenres: List<Genre> = emptyList()
)

data class FavoritedMediaModel(
	val movies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val shows: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val showGenres: List<Genre> = emptyList()
)

data class RatedMediaModel(
	val movies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val shows: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty())
)
