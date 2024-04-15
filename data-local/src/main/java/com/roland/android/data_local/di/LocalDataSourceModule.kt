package com.roland.android.data_local.di

import com.roland.android.data_local.data_source.LocalAuthDataSourceImpl
import com.roland.android.data_repository.auth.LocalAuthDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

	@Binds
	abstract fun bindAuthDataSource(
		localAuthDataSourceImpl: LocalAuthDataSourceImpl
	): LocalAuthDataSource

}