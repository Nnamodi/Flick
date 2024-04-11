package com.roland.android.data_remote.di

import com.roland.android.data_remote.BuildConfig
import com.roland.android.data_remote.auth.AuthService
import com.roland.android.data_remote.network.service.CastService
import com.roland.android.data_remote.network.service.MovieService
import com.roland.android.data_remote.network.service.TvShowService
import com.roland.android.data_remote.utils.Constants.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

	@Provides
	fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
		.readTimeout(60, TimeUnit.SECONDS)
		.connectTimeout(60, TimeUnit.SECONDS)
		.addInterceptor(
			Interceptor { chain: Interceptor.Chain ->
				val request = chain.request()
					.newBuilder()
					.header("Authorization", "Bearer ${BuildConfig.ACCESS_TOKEN}")
					.build()
				chain.proceed(request)
			}
		).build()

	@Provides
	fun providesMoshi(): Moshi = Moshi.Builder()
		.add(KotlinJsonAdapterFactory())
		.build()

	@Provides
	fun providesRetrofit(
		okHttpClient: OkHttpClient,
		moshi: Moshi
	): Retrofit = Retrofit.Builder()
		.baseUrl(BASE_URL)
		.client(okHttpClient)
		.addConverterFactory(MoshiConverterFactory.create(moshi))
		.build()

	@Provides
	fun providesAuthService(retrofit: Retrofit): AuthService {
		return retrofit.create(AuthService::class.java)
	}

	@Provides
	fun providesCastService(retrofit: Retrofit): CastService {
		return retrofit.create(CastService::class.java)
	}

	@Provides
	fun providesMovieService(retrofit: Retrofit): MovieService {
		return retrofit.create(MovieService::class.java)
	}

	@Provides
	fun providesTvShowService(retrofit: Retrofit): TvShowService {
		return retrofit.create(TvShowService::class.java)
	}

	@Provides
	fun providesCoroutineScope(): CoroutineScope {
		return CoroutineScope(Dispatchers.IO)
	}

}