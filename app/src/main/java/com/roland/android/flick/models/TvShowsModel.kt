package com.roland.android.flick.models

import androidx.paging.PagingData
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import kotlinx.coroutines.flow.MutableStateFlow

data class TvShowsModel(
	val trending: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val popular: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val airingToday: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val topRated: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val soonToAir: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
)

data class FurtherTvShowsModel(
	val bollywood: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val anime: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val genres: GenreList = GenreList(),
)

data class TvShowDetailsModel(
	val details: Series = Series(),
	val recommendedShows: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val similarShows: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movieGenres: GenreList = GenreList(),
	val seriesGenres: GenreList = GenreList()
)

data class SeasonDetailsModel(
	val season: Season = Season(),
	val episode: Episode = Episode(),
	val showCasts: MovieCredits = MovieCredits(),
)
