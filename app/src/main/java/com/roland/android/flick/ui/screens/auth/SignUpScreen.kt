package com.roland.android.flick.ui.screens.auth

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.domain.entity.auth_response.RequestToken
import com.roland.android.flick.R
import com.roland.android.flick.models.AccountModel
import com.roland.android.flick.models.ResponseModel
import com.roland.android.flick.models.TokenModel
import com.roland.android.flick.state.AuthUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.SignUpButton
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.ChromeTabUtils
import com.roland.android.flick.utils.Constants
import com.roland.android.flick.utils.Constants.NavigationBarHeight
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
				paddingValues = paddingValues,
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
	paddingValues: PaddingValues,
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
	val bottomPadding = if (inPortraitMode) NavigationBarHeight else 0.dp

	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.BottomCenter
	) {
		Image(
			painter = painterResource(R.drawable.sign_in_background),
			contentDescription = null,
			modifier = Modifier.fillMaxSize(),
			contentScale = ContentScale.Crop
		)
		Column(
			modifier = Modifier
				.padding(bottom = bottomPadding + paddingValues.calculateBottomPadding())
				.padding(32.dp)
				.clip(MaterialTheme.shapes.large)
				.background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
				.padding(20.dp)
				.animateContentSize(),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = stringResource(R.string.app_name),
				modifier = Modifier.padding(bottom = 20.dp),
				color = MaterialTheme.colorScheme.onBackground,
				fontWeight = FontWeight.Bold,
				style = MaterialTheme.typography.titleLarge
			)
			Text(
				text = stringResource(R.string.sign_up_appeal),
				modifier = Modifier.padding(bottom = 30.dp),
				color = MaterialTheme.colorScheme.onBackground
			)
			SignUpButton(
				loading = isLoading,
				failed = requestFailed || approvalCancelled,
				completed = tokenData?.accessTokenResponse?.success == true,
				modifier = Modifier.padding(horizontal = 20.dp),
				onClick = { action(AuthActions.GenerateRequest) }
			)
		}
	}
}

@Preview
@Composable
private fun SignUpScreenPreview() {
	FlickTheme(darkTheme = true) {
		val uiState = AuthUiState(
			tokenData = State.Success(TokenModel()),
			responseData = State.Success(ResponseModel()),
			accountData = State.Success(AccountModel())
		)
		SignUpScreen(uiState) {}
	}
}
