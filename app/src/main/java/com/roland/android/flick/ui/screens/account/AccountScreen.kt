package com.roland.android.flick.ui.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.roland.android.flick.models.AccountModel
import com.roland.android.flick.models.ResponseModel
import com.roland.android.flick.state.AuthUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.screens.CommonScreen

@Composable
fun AccountScreen(
	uiState: AuthUiState,
	authAction: (AuthActions) -> Unit
) {
	Scaffold { paddingValues ->
		CommonScreen(
			state1 = uiState.tokenData,
			state2 = uiState.accountData,
			state3 = uiState.responseData,
			state4 = State.Success(""),
			state5 = State.Success(""),
			state6 = State.Success(""),
			paddingValues = paddingValues,
			loadingScreen = {
				Column(
					modifier = Modifier.fillMaxSize(),
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally
				) { Text("Loading...") }
			}
		) { tokenData, accountData, responseData, _, _, _ ->
			AccountDetailsScreen(
				accountData = accountData,
				responseData = responseData,
				authAction = authAction
			)
		}
	}
}

@Composable
private fun AccountDetailsScreen(
	accountData: AccountModel,
	responseData: ResponseModel,
	authAction: (AuthActions) -> Unit
) {
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) { Text("Account Screen") }
}