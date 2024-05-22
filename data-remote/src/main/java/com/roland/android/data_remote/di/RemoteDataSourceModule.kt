package com.roland.android.data_remote.di

import com.roland.android.data_remote.auth.AuthUtilImpl
import com.roland.android.data_remote.data_source.RemoteCastSourceImpl
import com.roland.android.data_remote.data_source.RemoteMovieSourceImpl
import com.roland.android.data_remote.data_source.RemoteTvShowSourceImpl
import com.roland.android.data_repository.data_source.remote.AuthUtil
import com.roland.android.data_repository.data_source.remote.RemoteCastSource
import com.roland.android.data_repository.data_source.remote.RemoteMovieSource
import com.roland.android.data_repository.data_source.remote.RemoteTvShowSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

	@Binds
	abstract fun bindAuthUtil(authUtilImpl: AuthUtilImpl): AuthUtil

	@Binds
	abstract fun bindRemoteCastSource(remoteCastSourceImpl: RemoteCastSourceImpl): RemoteCastSource

	@Binds
	abstract fun bindRemoteMovieSource(remoteMovieSourceImpl: RemoteMovieSourceImpl): RemoteMovieSource

	@Binds
	abstract fun bindRemoteTvShowSource(remoteTvShowSourceImpl: RemoteTvShowSourceImpl): RemoteTvShowSource

}