package com.roland.android.flick.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkConnectivityImpl @Inject constructor(context: Context) : NetworkConnectivity {
	private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	private val networkRequest = NetworkRequest.Builder()
		.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
		.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
		.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
		.build()

	override fun observe(): Flow<NetworkConnectivity.Status> {
		return callbackFlow {
			val callback = object : ConnectivityManager.NetworkCallback() {
				override fun onAvailable(network: Network) {
					super.onAvailable(network)
					launch { send(NetworkConnectivity.Status.Online) }
				}

				override fun onLost(network: Network) {
					super.onLost(network)
					launch { send(NetworkConnectivity.Status.Offline) }
				}

				override fun onUnavailable() {
					super.onUnavailable()
					launch { send(NetworkConnectivity.Status.Offline) }
				}
			}

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
				connectivityManager.requestNetwork(networkRequest, callback)
			} else {
				connectivityManager.registerDefaultNetworkCallback(callback)
			}
			awaitClose {
				connectivityManager.unregisterNetworkCallback(callback)
			}
		}.distinctUntilChanged()
	}
}