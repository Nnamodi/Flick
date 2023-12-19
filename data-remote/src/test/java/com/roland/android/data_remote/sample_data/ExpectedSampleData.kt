package com.roland.android.data_remote.sample_data

import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.Dates
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series

object ExpectedSampleData {

	// Movies
	private val movie1 = Movie(id = 0, title = "Oppenheimer")
	private val movie2 = Movie(id = 1, title = "Barbie")
	private val movie3 = Movie(id = 2, title = "The Burial")
	private val movie4 = Movie(id = 3, title = "Infinite")

	val trendingMovies = MovieList(dates = Dates(), results = listOf(movie1, movie2), totalResults = 2)
	val popularMovies = MovieList(dates = Dates(), results = listOf(movie1, movie2, movie3, movie4), totalResults = 4)
	val nowPlayingMovies = MovieList(dates = Dates(), results = listOf(movie1, movie2, movie4), totalResults = 3)
	val topRatedMovies = MovieList(dates = Dates(), results = listOf(movie1, movie4), totalResults = 2)
	val upcomingMovies = MovieList(dates = Dates(), results = listOf(movie3), totalResults = 1)
	val bollywoodMovies = MovieList(dates = Dates(), results = listOf(movie1, movie2, movie3), totalResults = 3)
	val animeCollections = MovieList(dates = Dates(), results = listOf(movie1, movie3), totalResults = 2)
	val recommendedMovies = MovieList(dates = Dates(), results = listOf(movie4), totalResults = 1)
	val similarMovies = MovieList(dates = Dates(), results = listOf(movie2, movie3, movie4), totalResults = 3)
	val movieDetails = MovieDetails(id = 2, title = movie1.title)

	// TvShows
	private val show1 = Movie(id = 0, title = "The Blacklist")
	private val show2 = Movie(id = 1, title = "Silicon Valley")
	private val show3 = Movie(id = 2, title = "Money Heist")
	private val show4 = Movie(id = 3, title = "911")

	val topRatedShows = MovieList(dates = Dates(), results = listOf(show1, show2), totalResults = 2)
	val popularShows = MovieList(dates = Dates(), results = listOf(show1, show2, show3, show4), totalResults = 4)
	val showsAiringToday = MovieList(dates = Dates(), results = listOf(show1, show2, show4), totalResults = 3)
	val showsSoonToAir = MovieList(dates = Dates(), results = listOf(show1, show4), totalResults = 2)
	val recommendedShows = MovieList(dates = Dates(), results = listOf(show2, show3), totalResults = 2)
	val similarShows = MovieList(dates = Dates(), results = listOf(show2, show3, show4), totalResults = 3)
	val showDetails = Series(name = "24 hours")
	val seasonDetails = Season(seasonNumber = 2)
	val episodeDetails = Episode(episodeNumber = 1)

	// Other
	val genreList = listOf(Genre(name = "Action"), Genre(name = "Animation"), Genre(name = "Comedy"))
	val movieCredits = MovieCredits(casts = listOf(Cast(name = "Jack"), Cast(name = "Downey"), Cast(name = "William")))
	val movieCast = movieCredits.casts[0]

}