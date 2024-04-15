package com.roland.android.data_local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.roland.android.data_local.data_source.LocalAuthDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {

	@Provides
	fun provideLocalAuthDataSourceImpl(
		@ApplicationContext context: Context
	) = LocalAuthDataSourceImpl(context.dataStore)

}