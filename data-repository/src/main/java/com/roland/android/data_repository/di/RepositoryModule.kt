package com.roland.android.data_repository.di

import com.roland.android.data_repository.auth.AuthRepositoryImpl
import com.roland.android.data_repository.media_repository.CastRepositoryImpl
import com.roland.android.data_repository.media_repository.MovieRepositoryImpl
import com.roland.android.data_repository.media_repository.TvShowRepositoryImpl
import com.roland.android.domain.repository.AuthRepository
import com.roland.android.domain.repository.CastRepository
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

	@Binds
	abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

	@Binds
	abstract fun bindMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository

	@Binds
	abstract fun bindCastRepository(castRepositoryImpl: CastRepositoryImpl): CastRepository

	@Binds
	abstract fun bindTvShowRepository(tvShowRepositoryImpl: TvShowRepositoryImpl): TvShowRepository

}