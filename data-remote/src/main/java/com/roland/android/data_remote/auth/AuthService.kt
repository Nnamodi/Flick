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
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

	@POST("/4/auth/request_token")
	@FormUrlEncoded
	suspend fun generateRequestToken(
		@Field("redirect_to") redirectTo: String = "android://com.roland.android.flick/ui/MainActivity"
	): RequestTokenResponseModel

	@POST("/4/auth/access_token")
	suspend fun requestAccessToken(
		@Body requestToken: RequestTokenModel
	): AccessTokenResponseModel

	@POST("/3/authentication/session/convert/4")
	suspend fun createSession(
		@Body accessToken: AccessTokenModel
	): SessionIdResponseModel

	@GET("/3/account")
	suspend fun getAccountDetails(
		@Query("session_id") sessionId: String
	): AccountDetailsModel

	@DELETE("/3/authentication/session")
	suspend fun deleteSession(
		@Body sessionId: SessionIdModel
	): SessionIdResponseModel

	@DELETE("/4/auth/access_token")
	suspend fun logout(
		@Body accessToken: AccessTokenModel
	): ResponseModel

}
