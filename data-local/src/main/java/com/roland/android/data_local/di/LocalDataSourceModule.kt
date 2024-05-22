package com.roland.android.data_local.di

import com.roland.android.data_local.data_source.LocalAuthDataSourceImpl
import com.roland.android.data_local.data_source.SettingsDataSourceImpl
import com.roland.android.data_repository.data_source.local.LocalAuthDataSource
import com.roland.android.data_repository.data_source.local.SettingsDataSource
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

	@Binds
	abstract fun bindSettingsDataSource(
		settingsDataSourceImpl: SettingsDataSourceImpl
	): SettingsDataSource

}