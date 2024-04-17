package com.roland.android.flick.ui.screens.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.flick.models.TokenModel
import com.roland.android.flick.state.AuthUiState
import com.roland.android.flick.ui.components.SignUpButton
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.utils.ChromeTabUtils
import com.roland.android.flick.utils.Constants
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize

@Composable
fun SignUpScreen(
	uiState: AuthUiState,
	authAction: (AuthActions) -> Unit
) {
	Scaffold { paddingValues ->
		CommonScreen(
			state = uiState.tokenData,
			paddingValues = paddingValues,
		) { tokenData, isLoading, errorMessage ->
			SignUpScreen(
				tokenData = tokenData,
				isLoading = isLoading,
				requestFailed = errorMessage != null,
				action = authAction
			)
		}
	}
}

@Composable
private fun SignUpScreen(
	tokenData: TokenModel?,
	isLoading: Boolean,
	requestFailed: Boolean,
	action: (AuthActions) -> Unit
) {
	var tokenRequested by rememberSaveable { mutableStateOf(false) }
	var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
	val lifecycleOwner = LocalLifecycleOwner.current
	val chromeTabUtils = ChromeTabUtils(LocalContext.current)

	// redirect user to website to authorize request_token once generated
	LaunchedEffect(tokenData) {
		val requestTokenFetched = tokenData?.requestTokenResponse?.success == true
		if (!requestTokenFetched) return@LaunchedEffect
		chromeTabUtils.launchUrl(
			buildString {
				append(Constants.TOKEN_AUTHORIZATION_URL)
				append(tokenData?.requestTokenResponse?.requestToken)
			}
		)
	}

	// fetch access_token once request_token is authorized
	LaunchedEffect(lifecycle) {
		val requestTokenFetched = tokenData?.requestTokenResponse?.success == true
		if (lifecycle != Lifecycle.Event.ON_RESUME) return@LaunchedEffect
		if (!tokenRequested && !requestTokenFetched) return@LaunchedEffect

		val requestToken = RequestToken(tokenData?.requestTokenResponse?.requestToken.orEmpty())
		action(AuthActions.RequestAccessToken(requestToken))
	}

	DisposableEffect(lifecycleOwner) {
		val observer = LifecycleEventObserver { _, event ->
			lifecycle = event
		}
		lifecycleOwner.lifecycle.addObserver(observer)

		onDispose {
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}

	val windowSize = rememberWindowSize()
	val inPortraitMode by remember(windowSize) {
		derivedStateOf { windowSize.width == WindowType.Portrait }
	}
	val bottomPadding = if (inPortraitMode) Constants.NavigationBarHeight else 0.dp

	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(bottom = bottomPadding),
		contentAlignment = Alignment.BottomCenter
	) {
		// Random movie image for the background
		SignUpButton(
			loading = isLoading,
			failed = requestFailed,
			completed = tokenData?.accessTokenResponse?.success == true,
			onClick = {
				tokenRequested = true
				action(AuthActions.GenerateRequest)
			}
		)
	}
}