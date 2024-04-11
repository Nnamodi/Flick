package com.roland.android.data_remote.auth

import com.roland.android.data_remote.network.model.auth_response.AccessTokenModel
import com.roland.android.data_remote.network.model.auth_response.AccessTokenResponseModel
import com.roland.android.data_remote.network.model.auth_response.AccountDetailsModel
import com.roland.android.data_remote.network.model.auth_response.RequestTokenModel
import com.roland.android.data_remote.network.model.auth_response.RequestTokenResponseModel
import com.roland.android.data_remote.network.model.auth_response.ResponseModel
import com.roland.android.data_remote.network.model.auth_response.SessionIdModel
import com.roland.android.data_remote.network.model.auth_response.SessionIdResponseModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {

	@POST("/4/auth/request_token")
	fun generateRequestToken(): RequestTokenResponseModel

	@POST("/4/auth/access_token")
	fun requestAccessToken(
		@Body requestToken: RequestTokenModel
	): AccessTokenResponseModel

	@POST("/3/authentication/session/convert/4")
	fun createSession(
		@Body accessToken: AccessTokenModel
	): SessionIdResponseModel

	@GET("/3/account/{account_id}")
	fun getAccountDetails(
		@Path("account_id") accountId: String
	): AccountDetailsModel

	@DELETE("/3/authentication/session/convert/4")
	fun deleteSession(
		@Body sessionId: SessionIdModel
	): SessionIdResponseModel

	@DELETE("/4/auth/access_token")
	fun logout(
		@Body accessToken: AccessTokenModel
	): ResponseModel

}
