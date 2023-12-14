package com.roland.android.data_repository.data_source

import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.MovieCredits
import kotlinx.coroutines.flow.Flow

interface RemoteCastSource {

	fun fetchMovieCasts(movieId: Int): Flow<MovieCredits>

	fun fetchCastDetails(personId: Int): Flow<Cast>

}
