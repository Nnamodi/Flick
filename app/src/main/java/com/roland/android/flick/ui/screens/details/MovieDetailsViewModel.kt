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
import com.roland.android.domain.repository.AutoStreamOptions.Always
import com.roland.android.domain.repository.AutoStreamOptions.Wifi
import com.roland.android.domain.usecase.GetCastDetailsUseCase
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetSeasonDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import com.roland.android.domain.usecase.MediaActions
import com.roland.android.domain.usecase.MediaCategory
import com.roland.android.domain.usecase.MediaUtilUseCase
import com.roland.android.flick.R
import com.roland.android.flick.models.accountSessionId
import com.roland.android.flick.models.updatedMediaCategory
import com.roland.android.flick.models.userAccountDetails
import com.roland.android.flick.state.MovieDetailsUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.state.autoReloadData
import com.roland.android.flick.state.autoStreamTrailersOption
import com.roland.android.flick.utils.MediaUtil
import com.roland.android.flick.utils.ResponseConverter
import com.roland.android.flick.utils.network.NetworkConnectivity
import com.roland.android.flick.utils.network.NetworkConnectivity.Status
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
	private val networkConnectivity: NetworkConnectivity,
	private val mediaUtil: MediaUtil,
	private val converter: ResponseConverter
) : ViewModel() {
	private var _movieDetailsUiState = MutableStateFlow(MovieDetailsUiState())
	var movieDetailsUiState by mutableStateOf(_movieDetailsUiState.value); private set
	private var lastRequest by mutableStateOf<DetailsRequest?>(null)
	private var accountId by mutableIntStateOf(0)
	private var sessionId by mutableStateOf("")

	// cache details info to avoid unnecessary reload
	private var lastMovieIdFetched by mutableStateOf<Int?>(null)
	private var lastSeriesIdFetched by mutableStateOf<Int?>(null)
	private var lastCastIdFetched by mutableStateOf<Int?>(null)

	private var shouldAutoReloadData by mutableStateOf(true)
	private var autoStreamOption by mutableStateOf(Always)
	private var networkStatus by mutableStateOf(Status.Offline)

	init {
		viewModelScope.launch {
			accountSessionId.collect {
				sessionId = it ?: ""
			}
		}
		viewModelScope.launch {
			userAccountDetails.collect { user ->
				_movieDetailsUiState.update { it.copy(userIsLoggedIn = user?.id != 0) }
				user?.let { accountId = user.id }
			}
		}
		viewModelScope.launch {
			_movieDetailsUiState.collect {
				movieDetailsUiState = it
				Log.i("MovieDetailsInfo", "Fetched item details: $it")
			}
		}
		viewModelScope.launch {
			autoStreamTrailersOption.collect {
				autoStreamOption = it
				setAutoStreamOption()
			}
		}
		viewModelScope.launch {
			autoReloadData.collect {
				shouldAutoReloadData = it
			}
		}
		viewModelScope.launch {
			networkConnectivity.observe().collect { status ->
				networkStatus = status
				setAutoStreamOption()

				if (!shouldAutoReloadData || (status == Status.Offline) ||
					((movieDetailsUiState.movieDetails !is State.Error) &&
					(movieDetailsUiState.tvShowDetails !is State.Error) &&
					(movieDetailsUiState.castDetails !is State.Error))) return@collect
				retryLastRequest()
			}
		}
	}

	private fun setAutoStreamOption() {
		val autoStreamTrailer = when {
			autoStreamOption == Always -> true
			(autoStreamOption == Wifi) && (networkStatus == Status.OnWifi) -> true
			else -> false
		}
		_movieDetailsUiState.update { it.copy(autoStreamTrailer = autoStreamTrailer) }
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
		when (action) {
			is MovieDetailsActions.AddToWatchlist -> {
				mediaUtil.watchlistMedia(
					accountId = accountId,
					sessionId = sessionId,
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
					sessionId = sessionId,
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
					sessionId = sessionId,
					mediaActions = MediaActions.Rate(mediaId, rateValue)
				)
			)
				.map { converter.convertResponse(it) }
				.collect { response ->
					_movieDetailsUiState.update { it.copy(response = response) }
					if (response !is State.Success) return@collect
					updatedMediaCategory.value = MediaCategory.Rated
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