package com.roland.android.flick.ui.screens.account

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
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
				loading = isLoading,
				requestFailed = errorMessage != null,
				intentData = uiState.intentData,
				activityResumed = uiState.activityResumed,
				action = authAction
			)
		}
	}
}

@Composable
private fun SignUpScreen(
	tokenData: TokenModel?,
	loading: Boolean,
	requestFailed: Boolean,
	intentData: Uri?,
	activityResumed: Boolean,
	action: (AuthActions) -> Unit
) {
	var isLoading by rememberSaveable { mutableStateOf(loading) }
	var customTabOpened by rememberSaveable { mutableStateOf(false) }
	var approvalCancelled by rememberSaveable { mutableStateOf(false) }
	val chromeTabUtils = ChromeTabUtils(LocalContext.current)

	// redirect user to website to authorize request_token once generated
	LaunchedEffect(tokenData) {
		val requestTokenFetched = tokenData?.requestTokenResponse?.success == true
		if (!requestTokenFetched) return@LaunchedEffect
		customTabOpened = true; isLoading = true
		chromeTabUtils.launchUrl(
			buildString {
				append(Constants.TOKEN_AUTHORIZATION_URL)
				append(tokenData?.requestTokenResponse?.requestToken)
			}
		)
	}

	// fetch access_token once request_token is authorized
	LaunchedEffect(intentData) {
		val requestTokenFetched = tokenData?.requestTokenResponse?.success == true
		if (intentData == null) return@LaunchedEffect
		if (!customTabOpened && !requestTokenFetched) return@LaunchedEffect
		customTabOpened = false

		val requestToken = RequestToken(tokenData?.requestTokenResponse?.requestToken.orEmpty())
		action(AuthActions.RequestAccessToken(requestToken))
	}

	// when user cancels the approval
	LaunchedEffect(activityResumed) {
		if (!activityResumed) return@LaunchedEffect
		approvalCancelled = customTabOpened && intentData == null
		if (!approvalCancelled) return@LaunchedEffect
		isLoading = false
		action(AuthActions.AuthorizationCancelled)
		customTabOpened = false
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
			failed = requestFailed || approvalCancelled,
			completed = tokenData?.accessTokenResponse?.success == true,
			onClick = { action(AuthActions.GenerateRequest) }
		)
	}
}