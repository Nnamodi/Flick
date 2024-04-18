package com.roland.android.domain.entity.auth_response

data class AccountDetails(
	val id: Int = 0,
	val avatar: Avatar = Avatar(),
	val name: String = "",
	val username: String = ""
)

data class Avatar(
	val gravatar: Gravatar = Gravatar(),
	val tmdbAvatar: TmdbAvatar = TmdbAvatar()
)

data class Gravatar(
	val hash: String = ""
)

data class TmdbAvatar(
	val avatarPath: String? = null
)