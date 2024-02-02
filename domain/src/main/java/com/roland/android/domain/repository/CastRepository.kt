package com.roland.android.domain.repository

import com.roland.android.domain.entity.CastDetails
import com.roland.android.domain.entity.MovieCredits
import kotlinx.coroutines.flow.Flow

interface CastRepository {

	fun fetchMovieCasts(movieId: Int): Flow<MovieCredits>

	fun fetchCastDetails(personId: Int): Flow<CastDetails>

}
