package com.roland.android.data_remote.sample_data

import com.roland.android.data_remote.network.model.CastModel
import com.roland.android.data_remote.network.model.EpisodeModel
import com.roland.android.data_remote.network.model.GenreListModel
import com.roland.android.data_remote.network.model.GenreModel
import com.roland.android.data_remote.network.model.MovieCreditsModel
import com.roland.android.data_remote.network.model.MovieDetailsModel
import com.roland.android.data_remote.network.model.MovieListModel
import com.roland.android.data_remote.network.model.MovieModel
import com.roland.android.data_remote.network.model.SeasonModel
import com.roland.android.data_remote.network.model.SeriesModel

object RemoteSampleData {

	// Movies
	private val movie1 = MovieModel(id = 0, title = "Oppenheimer")
	private val movie2 = MovieModel(id = 1, title = "Barbie")
	private val movie3 = MovieModel(id = 2, title = "The Burial")
	private val movie4 = MovieModel(id = 3, title = "Infinite")

	val remoteTrendingMovies = MovieListModel(results = listOf(movie1, movie2), totalResults = 2)
	val remotePopularMovies = MovieListModel(results = listOf(movie1, movie2, movie3, movie4), totalResults = 4)
	val remoteNowPlayingMovies = MovieListModel(results = listOf(movie1, movie2, movie4), totalResults = 3)
	val remoteTopRatedMovies = MovieListModel(results = listOf(movie1, movie4), totalResults = 2)
	val remoteUpcomingMovies = MovieListModel(results = listOf(movie3), totalResults = 1)
	val remoteBollywoodMovies = MovieListModel(results = listOf(movie1, movie2, movie3), totalResults = 3)
	val remoteAnimeCollections = MovieListModel(results = listOf(movie1, movie3), totalResults = 2)
	val remoteRecommendedMovies = MovieListModel(results = listOf(movie4), totalResults = 1)
	val remoteSimilarMovies = MovieListModel(results = listOf(movie2, movie3, movie4), totalResults = 3)
	val remoteMovieDetails = MovieDetailsModel(id = 2, title = movie1.title)

	// TvShows
	private val show1 = MovieModel(id = 0, title = "The Blacklist")
	private val show2 = MovieModel(id = 1, title = "Silicon Valley")
	private val show3 = MovieModel(id = 2, title = "Money Heist")
	private val show4 = MovieModel(id = 3, title = "911")

	val remoteTrendingShows = MovieListModel(results = listOf(show2, show3, show4), totalResults = 3)
	val remotePopularShows = MovieListModel(results = listOf(show1, show2, show3, show4), totalResults = 4)
	val remoteShowsAiringToday = MovieListModel(results = listOf(show1, show2, show4), totalResults = 3)
	val remoteTopRatedShows = MovieListModel(results = listOf(show1, show2), totalResults = 2)
	val remoteShowsSoonToAir = MovieListModel(results = listOf(show1, show4), totalResults = 2)
	val remoteBollywoodShows = MovieListModel(results = listOf(show1, show2, show3, show4), totalResults = 4)
	val remoteAnimeShows = MovieListModel(results = listOf(show1, show3, show4), totalResults = 3)
	val remoteRecommendedShows = MovieListModel(results = listOf(show2, show3), totalResults = 2)
	val remoteSimilarShows = MovieListModel(results = listOf(show2, show3, show4), totalResults = 3)
	val remoteShowDetails = SeriesModel(name = "24 hours")
	val remoteSeasonDetails = SeasonModel(seasonNumber = 2)
	val remoteEpisodeDetails = EpisodeModel(episodeNumber = 1)

	// Other
	val remoteGenreList = GenreListModel(listOf(GenreModel(name = "Action"), GenreModel(name = "Animation"), GenreModel(name = "Comedy")))
	val remoteMovieCredits = MovieCreditsModel(casts = listOf(CastModel(name = "Jack"), CastModel(name = "Downey"), CastModel(name = "William")))
	val remoteMovieCast = remoteMovieCredits.casts[0]

}