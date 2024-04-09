package com.roland.android.data_remote.network.model.auth_response

import com.squareup.moshi.Json

data class AccountDetailsModel(
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "avatar")
	val avatar: AvatarModel = AvatarModel(),
	@Json(name = "name")
	val name: String = "",
	@Json(name = "username")
	val username: String = ""
)

data class AvatarModel(
	@Json(name = "gravatar")
	val gravatar: GravatarModel = GravatarModel(),
	@Json(name = "tmdb")
	val tmdbAvatar: TmdbAvatarModel = TmdbAvatarModel()
)

data class GravatarModel(
	@Json(name = "hash")
	val hash: String = ""
)

data class TmdbAvatarModel(
	@Json(name = "avatar_path")
	val avatarPath: String = ""
)
