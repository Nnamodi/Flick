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
	val movie1 = Movie(
		id = 0,
		title = "Oppenheimer",
		overview = "The story of J. Robert Oppenheimer's role in the development of the atomic bomb during World War II.",
		genreIds = listOf(11, 24, 5),
		voteAverage = 8.2,
		releaseDate = "2024-02-12"
	)
	private val movie2 = Movie(
		id = 1,
		title = "Spider-Man: Into the Spider-Realm",
		overview = "Teen Miles Markus becomes the Spider-Man of more universes and now joins with five spider-powered individuals from other climes to stop a threat for all live forms.",
		genreIds = listOf(11, 24, 7),
		voteAverage = 8.3,
		releaseDate = "2024-02-28"
	)
	private val movie3 = Movie(
		id = 2,
		title = "Barbie",
		overview = "An exploration in the human world by dolls who have lived in isolation of themselves since their existence in the unimaginably weird universe.",
		genreIds = listOf(5, 15, 24, 7),
		voteAverage = 7.4,
		releaseDate = "2024-01-31"
	)
	private val movie4 = Movie(
		id = 3,
		title = "The Burial",
		overview = "Relentlessly pursued by a powerful politician's daughter who will do anything to make him hers, a man slips down a dark, risky path to reclaim his life.",
		genreIds = listOf(5, 15, 7),
		voteAverage = 7.8,
		releaseDate = "2024-03-04"
	)
	val movie5 = Movie(
		id = 4,
		title = "Infinite",
		overview = "A robot that lives on a post-apocalyptic earth which was built to protect the life of his dying creator's beloved dog, it learns about love, friendship and more traits that makes it behave human.",
		genreIds = listOf(11, 24, 7),
		voteAverage = 8.3,
		releaseDate = "2024-02-28"
	)

	val trendingMovies = PagingData.from(listOf(movie1, movie2, movie3, movie4)).refactor()
	val popularMovies = PagingData.from(listOf(movie1, movie2)).refactor()
	val nowPlayingMovies = PagingData.from(listOf(movie1, movie2, movie4)).refactor()
	val topRatedMovies = PagingData.from(listOf(movie1, movie4)).refactor()
	val upcomingMovies = PagingData.from(listOf(movie1, movie2, movie3, movie4, movie5)).refactor()
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
	val genreList = GenreList(listOf(
		Genre(11, "Action"),
		Genre(16, "Animation"),
		Genre(5, "Comedy"),
		Genre(15, "Drama"),
		Genre(24, "Adventure"),
		Genre(7, "Thriller")
	))
	val movieCredits = MovieCredits(casts = listOf(Cast(name = "Jack"), Cast(name = "Downey"), Cast(name = "William")))
	val movieCast = movieCredits.casts[0]

}