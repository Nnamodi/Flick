package com.roland.android.flick.di

import android.content.Context
import com.roland.android.domain.usecase.UseCase
import com.roland.android.flick.utils.network.NetworkConnectivityImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

	@Provides
	fun providesUseCaseConfiguration() = UseCase.Configuration(Dispatchers.IO)

	@Provides
	fun providesNetworkConnectivityImpl(
		@ApplicationContext context: Context
	) = NetworkConnectivityImpl(context)

}