package com.roland.android.data_repository.data_source.remote

import com.roland.android.domain.entity.CastDetails
import com.roland.android.domain.entity.MovieCredits
import kotlinx.coroutines.flow.Flow

interface RemoteCastSource {

	fun fetchMovieCasts(movieId: Int): Flow<MovieCredits>

	fun fetchCastDetails(personId: Int): Flow<CastDetails>

}
