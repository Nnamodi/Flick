package com.roland.android.data_remote.data_source

import com.roland.android.data_remote.network.service.CastService
import com.roland.android.data_remote.utils.Converters.convertToCast
import com.roland.android.data_remote.utils.Converters.convertToMovieCredits
import com.roland.android.data_repository.data_source.RemoteCastSource
import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteCastSourceImpl @Inject constructor(
	private val castService: CastService
) : RemoteCastSource {

	override fun fetchMovieCasts(movieId: Int): Flow<MovieCredits> = flow {
		emit(castService.fetchMovieCasts(movieId))
	}.map { creditsModel ->
		convertToMovieCredits(creditsModel)
	}.catch {
		throw UseCaseException.CastException(it)
	}

	override fun fetchCastDetails(personId: Int): Flow<Cast> = flow {
		emit(castService.fetchCastDetails(personId))
	}.map { castModel ->
		convertToCast(castModel)
	}.catch {
		throw UseCaseException.CastException(it)
	}

}