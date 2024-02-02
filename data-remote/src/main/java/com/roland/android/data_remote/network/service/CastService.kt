package com.roland.android.data_remote.network.service

import com.roland.android.data_remote.network.model.CastDetailsModel
import com.roland.android.data_remote.network.model.MovieCreditsModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CastService {

	@GET("/3/movie/{movie_id}/credits")
	suspend fun fetchMovieCasts(
		@Path("movie_id") movieId: Int,
		@Query("language") language: String = "en_US"
	): MovieCreditsModel

	@GET("/3/person/{person_id}")
	suspend fun fetchCastDetails(
		@Path("person_id") personId: Int,
		@Query("language") language: String = "en_US"
	): CastDetailsModel

}
