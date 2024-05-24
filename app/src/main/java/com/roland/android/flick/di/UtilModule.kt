package com.roland.android.flick.di

import com.roland.android.flick.utils.network.NetworkConnectivity
import com.roland.android.flick.utils.network.NetworkConnectivityImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {

	@Binds
	abstract fun bindNetworkConnectivity(
		networkConnectivityImpl: NetworkConnectivityImpl
	) : NetworkConnectivity

}