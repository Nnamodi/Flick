package com.roland.android.domain.usecase

import com.roland.android.domain.entity.Cast
import com.roland.android.domain.repository.CastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCastDetailsUseCase @Inject constructor(
	configuration: Configuration,
	private val castRepository: CastRepository
) : UseCase<GetCastDetailsUseCase.Request, GetCastDetailsUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = castRepository
		.fetchCastDetails(request.personId).map {
			Response(it)
		}

	data class Request(val personId: Int) : UseCase.Request

	data class Response(val cast: Cast) : UseCase.Response

}
