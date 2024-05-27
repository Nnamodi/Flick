package com.roland.android.flick.utils.network

import kotlinx.coroutines.flow.Flow

interface NetworkConnectivity {

	fun observe(): Flow<Status>

	enum class Status {
		Online, OnWifi, Offline
	}

}