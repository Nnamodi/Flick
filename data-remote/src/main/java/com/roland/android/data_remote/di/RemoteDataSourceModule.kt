package com.roland.android.data_remote.di

import com.roland.android.data_remote.data_source.RemoteCastSourceImpl
import com.roland.android.data_remote.data_source.RemoteMovieSourceImpl
import com.roland.android.data_remote.data_source.RemoteTvShowSourceImpl
import com.roland.android.data_repository.data_source.RemoteCastSource
import com.roland.android.data_repository.data_source.RemoteMovieSource
import com.roland.android.data_repository.data_source.RemoteTvShowSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

	@Binds
	abstract fun bindRemoteCastSource(remoteCastSourceImpl: RemoteCastSourceImpl): RemoteCastSource

	@Binds
	abstract fun bindRemoteMovieSource(remoteMovieSourceImpl: RemoteMovieSourceImpl): RemoteMovieSource

	@Binds
	abstract fun bindRemoteTvShowSource(remoteTvShowSourceImpl: RemoteTvShowSourceImpl): RemoteTvShowSource

}