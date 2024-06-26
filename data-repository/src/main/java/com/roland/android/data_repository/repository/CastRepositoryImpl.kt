package com.roland.android.data_repository.repository

import com.roland.android.data_repository.data_source.remote.RemoteCastSource
import com.roland.android.domain.entity.CastDetails
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.repository.CastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CastRepositoryImpl @Inject constructor(
	private val remoteCastSource: RemoteCastSource
) : CastRepository {

	override fun fetchMovieCasts(movieId: Int): Flow<MovieCredits> = remoteCastSource.fetchMovieCasts(movieId)

	override fun fetchCastDetails(personId: Int): Flow<CastDetails> = remoteCastSource.fetchCastDetails(personId)

}