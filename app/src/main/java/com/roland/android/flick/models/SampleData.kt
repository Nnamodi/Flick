package com.roland.android.flick.models

import androidx.paging.PagingData
import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import com.roland.android.flick.utils.Extensions.refactor

object SampleData {

	// Movies
	private val movie1 = Movie(
		id = 0,
		title = "Oppenheimer",
		overview = "The story of J. Robert Oppenheimer's role in the development of the atomic bomb during World War II.",
		releaseDate = "2023-09-12"
	)
	private val movie2 = Movie(id = 1, title = "Barbie")
	private val movie3 = Movie(id = 2, title = "The Burial")
	private val movie4 = Movie(id = 3, title = "Infinite")

	val trendingMovies = PagingData.from(listOf(movie1, movie2, movie3, movie4)).refactor()
	val popularMovies = PagingData.from(listOf(movie1, movie2)).refactor()
	val nowPlayingMovies = PagingData.from(listOf(movie1, movie2, movie4)).refactor()
	val topRatedMovies = PagingData.from(listOf(movie1, movie4)).refactor()
	val upcomingMovies = PagingData.from(listOf(movie3)).refactor()
	val bollywoodMovies = PagingData.from(listOf(movie1, movie2, movie3)).refactor()
	val animeCollections = PagingData.from(listOf(movie1, movie3)).refactor()
	val recommendedMovies = PagingData.from(listOf(movie4)).refactor()
	val similarMovies = PagingData.from(listOf(movie2, movie3, movie4)).refactor()
	val movieDetails = MovieDetails(id = 2, title = movie1.title)

	// TvShows
	private val show1 = Movie(id = 0, title = "The Blacklist")
	private val show2 = Movie(id = 1, title = "Silicon Valley")
	private val show3 = Movie(id = 2, title = "Money Heist")
	private val show4 = Movie(id = 3, title = "911")

	val trendingShows = PagingData.from(listOf(show2, show3, show4)).refactor()
	val popularShows = PagingData.from(listOf(show1, show2, show3, show4)).refactor()
	val showsAiringToday = PagingData.from(listOf(show1, show2, show4)).refactor()
	val topRatedShows = PagingData.from(listOf(show1, show2)).refactor()
	val showsSoonToAir = PagingData.from(listOf(show1, show4)).refactor()
	val bollywoodShows = PagingData.from(listOf(show1, show2, show3, show4)).refactor()
	val animeShows = PagingData.from(listOf(show1, show3, show4)).refactor()
	val recommendedShows = PagingData.from(listOf(show2, show3)).refactor()
	val similarShows = PagingData.from(listOf(show2, show3, show4)).refactor()
	val showDetails = Series(name = "24 hours")
	val seasonDetails = Season(seasonNumber = 2)
	val episodeDetails = Episode(episodeNumber = 1)

	// Other
	val genreList = GenreList(listOf(Genre(name = "Action"), Genre(name = "Animation"), Genre(name = "Comedy")))
	val movieCredits = MovieCredits(casts = listOf(Cast(name = "Jack"), Cast(name = "Downey"), Cast(name = "William")))
	val movieCast = movieCredits.casts[0]

}