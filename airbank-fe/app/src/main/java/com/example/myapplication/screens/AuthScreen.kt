package com.example.myapplication.screens

import android.util.Log
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.viewmodel.AuthViewModel
import com.kakao.sdk.user.UserApiClient

@Composable
fun AuthScreen(navController: NavController) {
    val viewModel: AuthViewModel = viewModel() // Create an instance of AuthViewModel
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                setLoginClickListener(navController, viewModel) // Pass the ViewModel instance
            }
        }
    )
}

fun WebView.setLoginClickListener(navController: NavController ,viewModel: AuthViewModel) {
    // Check if KakaoTalk login is available
    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            viewModel.handlelogin(token, error, navController)
        }
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            viewModel.handlelogin(token, error, navController)
        }
    }
}