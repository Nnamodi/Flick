package com.roland.android.flick.di

import com.roland.android.domain.usecase.UseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

	@Provides
	fun providesUseCaseConfiguration() = UseCase.Configuration(Dispatchers.IO)

}