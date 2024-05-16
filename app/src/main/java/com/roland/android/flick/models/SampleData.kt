package com.roland.android.flick.models

import androidx.paging.PagingData
import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.CastDetails
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import com.roland.android.domain.entity.Video
import com.roland.android.flick.utils.Extensions.refactor

object SampleData {

	// Movies
	val movie1 = Movie(
		id = 0,
		title = "Oppenheimer",
		overview = "The story of J. Robert Oppenheimer's role in the development of the atomic bomb during World War II.",
		genreIds = listOf(11, 24, 5),
		voteAverage = 8.2,
		releaseDate = "2024-02-12",
		rating = 9
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
	val upcomingMovies = PagingData.from(listOf(movie1, movie2, movie3, movie4, movie5)).refactor()
	val bollywoodMovies = PagingData.from(listOf(movie1, movie2, movie3)).refactor()
	val animeCollections = PagingData.from(listOf(movie1, movie3)).refactor()
	val recommendedMovies = PagingData.from(listOf(movie4)).refactor()
	val similarMovies = PagingData.from(listOf(movie2, movie3, movie4)).refactor()
	val movieDetails = MovieDetails(
		id = 2,
		title = movie5.title,
		overview = movie5.overview,
		genres = listOf(
			Genre(11, "Action"),
			Genre(16, "Animation"),
			Genre(5, "Comedy"),
			Genre(15, "Drama")
		),
		voteAverage = movie5.voteAverage,
		releaseDate = movie5.releaseDate,
		runtime = 142,
		videos = listOf(
			Video(id = "0", name = "First Trailer"),
			Video(id = "1", name = "Behind the scene"),
			Video(id = "2", name = "Documentary"),
			Video(id = "3", name = "Interview of top casts")
		),
		credits = MovieCredits(
			cast = listOf(
				Cast(name = "Jack Jones", character = "Red"),
				Cast(name = "Downey Furry", character = "Pioneer"),
				Cast(name = "William Tarnish", character = "Aram"),
				Cast(name = "Theresa Shawn", character = "The man by the road side"),
				Cast(name = "Shengupta Schofield", character = "Farmer"),
				Cast(name = "Roland Nnam", character = "Nnamodi")
			)
		)
	)

	// TvShows
	val trendingShows = PagingData.from(listOf(movie2, movie3, movie4)).refactor()
	val showsSoonToAir = PagingData.from(listOf(movie1, movie4)).refactor()
	val bollywoodShows = PagingData.from(listOf(movie1, movie2, movie3, movie4)).refactor()
	val animeShows = PagingData.from(listOf(movie1, movie3, movie4)).refactor()
	val recommendedShows = PagingData.from(listOf(movie2, movie3)).refactor()
	val similarShows = PagingData.from(listOf(movie2, movie3, movie4)).refactor()
	val showDetails = Series(
		id = 2,
		name = "24 hours",
		overview = movie5.overview,
		firstAirDate = movie5.releaseDate ?: "",
		genres = listOf(
			Genre(11, "Action"),
			Genre(24, "Adventure"),
			Genre(5, "Comedy"),
			Genre(15, "Drama"),
			Genre(7, "Thriller")
		),
		numberOfSeasons = 16,
		voteAverage = movie3.voteAverage,
		videos = listOf(
			Video(id = "0", name = "First Trailer"),
			Video(id = "1", name = "Behind the scene"),
			Video(id = "2", name = "Documentary"),
			Video(id = "3", name = "Interview of top casts")
		),
		credits = MovieCredits(
			cast = listOf(
				Cast(name = "Roland Nnam", character = "Nnamodi"),
				Cast(name = "Theresa Shawn", character = "The man by the road side"),
				Cast(name = "Shengupta Schofield", character = "Farmer"),
				Cast(name = "William Tarnish", character = "Aram"),
				Cast(name = "Downey Furry", character = "Pioneer"),
				Cast(name = "Jack Jones", character = "Red")
			)
		)
	)
	val seasonDetails = Season(
		seasonNumber = 3,
		episodes = listOf(
			Episode(id = 0, name = "Episode 1", voteAverage = 6.4),
			Episode(id = 1, name = "Episode 2", voteAverage = 8.0),
			Episode(id = 2, name = "Episode 3", voteAverage = 7.2),
			Episode(id = 3, name = "Episode 4", voteAverage = 8.4),
			Episode(id = 4, name = "Episode 5", voteAverage = 5.3),
			Episode(id = 5, name = "Episode 6", voteAverage = 7.4),
			Episode(id = 6, name = "Episode 7", voteAverage = 5.8)
		)
	)

	// Other
	val genreList = listOf(
		Genre(11, "Action"),
		Genre(16, "Animation"),
		Genre(5, "Comedy"),
		Genre(15, "Drama"),
		Genre(24, "Adventure"),
		Genre(7, "Thriller")
	)
	val movieCastDetails = CastDetails(
		name = "Jack Jones",
		knownForDepartment = "Acting",
		biography = "Teen Miles Markus becomes the Spider-Man of more universes and now joins with five spider-powered individuals from other climes to stop a threat for all live forms." +
				"An exploration in the human world by dolls who have lived in isolation of themselves since their existence in the unimaginably weird universe." +
				"Relentlessly pursued by a powerful politician's daughter who will do anything to make him hers, a man slips down a dark, risky path to reclaim his life." +
				"A robot that lives on a post-apocalyptic earth which was built to protect the life of his dying creator's beloved dog, it learns about love, friendship and more traits that makes it behave human.",
		moviesAndShowsActed = listOf(movie1, movie2, movie3, movie4, movie5)
	)

}