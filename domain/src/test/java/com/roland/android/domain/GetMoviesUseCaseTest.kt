package com.roland.android.domain

import com.roland.android.domain.Constant.ANIME
import com.roland.android.domain.Constant.BOLLYWOOD
import com.roland.android.domain.Constant.COMEDY
import com.roland.android.domain.Constant.HALLYUWOOD
import com.roland.android.domain.Constant.NOLLYWOOD
import com.roland.android.domain.Constant.ROMEDY_MOVIES
import com.roland.android.domain.Constant.SCI_FI_MOVIES
import com.roland.android.domain.Constant.WAR_STORY_MOVIES
import com.roland.android.domain.SampleTestData.animeCollections
import com.roland.android.domain.SampleTestData.bollywoodMovies
import com.roland.android.domain.SampleTestData.genreList
import com.roland.android.domain.SampleTestData.koreanMovies
import com.roland.android.domain.SampleTestData.nigerianMovies
import com.roland.android.domain.SampleTestData.nowPlayingMovies
import com.roland.android.domain.SampleTestData.popularMovies
import com.roland.android.domain.SampleTestData.topRatedMovies
import com.roland.android.domain.SampleTestData.trendingMovies
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.usecase.GetMoviesByGenreUseCase
import com.roland.android.domain.usecase.GetMoviesByRegionUseCase
import com.roland.android.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class GetMoviesUseCaseTest {

	private val movieRepository = mock<MovieRepository>()
	private val moviesUseCase = GetMoviesUseCase(mock(), movieRepository)
	private val moviesByRegionUseCase = GetMoviesByRegionUseCase(mock(), movieRepository)
	private val moviesByGenreUseCase = GetMoviesByGenreUseCase(mock(), movieRepository)

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess() = runTest {
		whenever(movieRepository.fetchTrendingMovies()).thenReturn(flowOf(trendingMovies))
		whenever(movieRepository.fetchPopularMovies()).thenReturn(flowOf(popularMovies))
		whenever(movieRepository.fetchNowPlayingMovies()).thenReturn(flowOf(nowPlayingMovies))
		whenever(movieRepository.fetchTopRatedMovies()).thenReturn(flowOf(topRatedMovies))
		whenever(movieRepository.fetchMovieGenres()).thenReturn(flowOf(genreList))

		val response = moviesUseCase.process(GetMoviesUseCase.Request).first()
		assertEquals(
			GetMoviesUseCase.Response(
				trendingMovies,
				popularMovies,
				nowPlayingMovies,
				topRatedMovies,
				genreList
			),
			response
		)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess2() = runTest {
		whenever(movieRepository.fetchMoviesByRegion(NOLLYWOOD)).thenReturn(flowOf(nigerianMovies))
		whenever(movieRepository.fetchMoviesByRegion(HALLYUWOOD)).thenReturn(flowOf(koreanMovies))
		whenever(movieRepository.fetchMoviesByRegion(BOLLYWOOD)).thenReturn(flowOf(bollywoodMovies))

		val response = moviesByRegionUseCase.process(GetMoviesByRegionUseCase.Request).first()
		assertEquals(
			GetMoviesByRegionUseCase.Response(
				nigerianMovies, koreanMovies, bollywoodMovies
			),
			response
		)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testProcess3() = runTest {
		whenever(movieRepository.fetchMoviesByGenre(ANIME)).thenReturn(flowOf(animeCollections))
		whenever(movieRepository.fetchMoviesByGenre(COMEDY)).thenReturn(flowOf(animeCollections))
		whenever(movieRepository.fetchMoviesByGenre(WAR_STORY_MOVIES)).thenReturn(flowOf(animeCollections))
		whenever(movieRepository.fetchMoviesByGenre(ROMEDY_MOVIES)).thenReturn(flowOf(animeCollections))
		whenever(movieRepository.fetchMoviesByGenre(SCI_FI_MOVIES)).thenReturn(flowOf(animeCollections))

		val response = moviesByGenreUseCase.process(GetMoviesByGenreUseCase.Request).first()
		assertEquals(
			GetMoviesByGenreUseCase.Response(
				animeCollections,
				animeCollections,
				animeCollections,
				animeCollections,
				animeCollections
			),
			response
		)
	}

}