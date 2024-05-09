package com.roland.android.flick.ui.screens.details

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetCastDetailsUseCase
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetSeasonDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import com.roland.android.domain.usecase.MediaActions
import com.roland.android.domain.usecase.MediaUtilUseCase
import com.roland.android.flick.R
import com.roland.android.flick.models.accountMediaUpdated
import com.roland.android.flick.models.userAccountDetails
import com.roland.android.flick.state.MovieDetailsUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.utils.MediaUtil
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
	private val movieDetailsUseCase: GetMovieDetailsUseCase,
	private val tvShowDetailsUseCase: GetTvShowDetailsUseCase,
	private val seasonDetailsUseCase: GetSeasonDetailsUseCase,
	private val castDetailsUseCase: GetCastDetailsUseCase,
	private val mediaUtilUseCase: MediaUtilUseCase,
	private val mediaUtil: MediaUtil,
	private val converter: ResponseConverter
) : ViewModel() {
	private var _movieDetailsUiState = MutableStateFlow(MovieDetailsUiState())
	var movieDetailsUiState by mutableStateOf(_movieDetailsUiState.value); private set
	private var lastRequest by mutableStateOf<DetailsRequest?>(null)
	private var accountId by mutableIntStateOf(0)

	// cache details info to avoid unnecessary reload
	private var lastMovieIdFetched by mutableStateOf<Int?>(null)
	private var lastSeriesIdFetched by mutableStateOf<Int?>(null)
	private var lastCastIdFetched by mutableStateOf<Int?>(null)

	init {
		viewModelScope.launch {
			userAccountDetails.collect { user ->
				user?.let {
					accountId = user.id
					if (user.id == 0) return@let
					_movieDetailsUiState.update { it.copy(userIsLoggedIn = true) }
				}
			}
		}
		viewModelScope.launch {
			_movieDetailsUiState.collect {
				movieDetailsUiState = it
				Log.i("MovieDetailsInfo", "Fetched item details: $it")
			}
		}
	}

	fun detailsRequest(request: DetailsRequest) {
		Log.i("NavigationInfo", "Action: $request")
		when (request) {
			is DetailsRequest.GetMovieDetails -> getMovieDetails(request.movieId)
			is DetailsRequest.GetTvShowDetails -> getTvShowDetails(request.seriesId)
			is DetailsRequest.GetSeasonDetails -> getSeasonDetails(request.seriesId, request.seasonNumber)
			is DetailsRequest.GetCastDetails -> getCastDetails(request.personId)
			is DetailsRequest.Retry -> retryLastRequest()
		}
		if (request is DetailsRequest.Retry) return
		lastRequest = request
	}

	private fun getMovieDetails(movieId: Int) {
		if (movieId == lastMovieIdFetched) return
		_movieDetailsUiState.update { it.copy(movieDetails = null) }
		viewModelScope.launch {
			movieDetailsUseCase.execute(GetMovieDetailsUseCase.Request(movieId))
				.map { converter.convertMovieDetailsData(it) }
				.collect { data ->
					_movieDetailsUiState.update { it.copy(movieDetails = data) }
					if (data is State.Success) lastMovieIdFetched = movieId
				}
		}
	}

	private fun getTvShowDetails(seriesId: Int) {
		if (seriesId == lastSeriesIdFetched) return
		_movieDetailsUiState.update { it.copy(tvShowDetails = null) }
		viewModelScope.launch {
			tvShowDetailsUseCase.execute(GetTvShowDetailsUseCase.Request(seriesId))
				.map { converter.convertTvShowDetailsData(it) }
				.collect { data ->
					_movieDetailsUiState.update { it.copy(tvShowDetails = data) }
					if (data is State.Success) lastSeriesIdFetched = seriesId
				}
		}
		getSeasonDetails(seriesId = seriesId, seasonNumber = 1)
	}

	private fun getSeasonDetails(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int = 1
	) {
		_movieDetailsUiState.update {
			it.copy(seasonDetails = null, selectedSeasonNumber = seasonNumber)
		}
		viewModelScope.launch {
			seasonDetailsUseCase.execute(
				GetSeasonDetailsUseCase.Request(
					seriesId, seasonNumber, episodeNumber
				)
			)
				.map { converter.convertSeasonDetailsData(it) }
				.collect { data ->
					_movieDetailsUiState.update { it.copy(seasonDetails = data) }
				}
		}
	}

	private fun getCastDetails(personId: Int) {
		if (personId == lastCastIdFetched) return
		_movieDetailsUiState.update { it.copy(castDetails = null) }
		viewModelScope.launch {
			castDetailsUseCase.execute(
				GetCastDetailsUseCase.Request(personId)
			)
				.map { converter.convertCastDetailsData(it) }
				.collect { data ->
					_movieDetailsUiState.update { it.copy(castDetails = data) }
					if (data is State.Success) lastCastIdFetched = personId
				}
		}
	}

	private fun retryLastRequest() {
		lastRequest?.let { detailsRequest(it) }
	}

	fun detailsAction(action: MovieDetailsActions?) {
		action?.let {
			_movieDetailsUiState.update { it.copy(response = null) }
		}
		accountMediaUpdated.value = false
		when (action) {
			is MovieDetailsActions.AddToWatchlist -> {
				mediaUtil.watchlistMedia(
					accountId = accountId,
					mediaId = action.mediaId,
					mediaType = action.mediaType,
					watchlist = true,
					result = { response ->
						_movieDetailsUiState.update { it.copy(response = response) }
					}
				)
			}
			is MovieDetailsActions.FavoriteMedia -> {
				mediaUtil.favoriteMedia(
					accountId = accountId,
					mediaId = action.mediaId,
					mediaType = action.mediaType,
					favorite = true,
					result = { response ->
						_movieDetailsUiState.update { it.copy(response = response) }
					}
				)
			}
			is MovieDetailsActions.RateMedia -> {
				rateMedia(
					mediaId = action.mediaId,
					mediaType = action.mediaType,
					rateValue = action.rateValue
				)
			}
			is MovieDetailsActions.Share -> shareUrl(action.mediaUrl, action.context)
			null -> {
				_movieDetailsUiState.update { it.copy(response = null) }
			}
		}
	}

	private fun rateMedia(
		mediaId: Int,
		mediaType: String,
		rateValue: Float
	) {
		viewModelScope.launch {
			mediaUtilUseCase.execute(
				MediaUtilUseCase.Request(
					mediaType = mediaType,
					mediaActions = MediaActions.Rate(mediaId, rateValue)
				)
			)
				.map { converter.convertResponse(it) }
				.collect { response ->
					_movieDetailsUiState.update { it.copy(response = response) }
					if (response !is State.Success) return@collect
					accountMediaUpdated.value = true
				}
		}
	}

	private fun shareUrl(mediaUrl: String, context: Context) {
		Intent(Intent.ACTION_SEND).apply {
			type = "text/plain"
			putExtra(Intent.EXTRA_TEXT, mediaUrl)
		}.also { intent ->
			val chooserIntent = Intent.createChooser(
				intent, context.getString(R.string.chooser_title)
			)
			context.startActivity(chooserIntent)
		}
	}

}