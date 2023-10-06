package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.AirbankApplication
import com.example.myapplication.model.POSTLoginRequest
import com.example.myapplication.model.POSTLoginResponse
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.screens.BottomNavItem
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private val TAG = "KAKAO LOGIN"
    private val repository = AuthRepository(viewModelScope) // Create a repository for data operations

    fun handlelogin(token: OAuthToken?, error: Throwable?, navController: NavController) {
        if (error != null) {
            Log.e(TAG, "로그인 실패", error)
            // Handle login failure here
        } else if (token != null) {
            Log.i(TAG, "로그인 성공 ${token.accessToken}")
            // After successful login, request user information
            retrieveUserInfo(token, navController)
        }
    }
    private fun retrieveUserInfo(token: OAuthToken, navController: NavController) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.retrieveUserInfo(token) { postLoginRequest: POSTLoginRequest ->
                // Handle the retrieved login request data
                performLoginRequest(postLoginRequest, navController)
            }
        }
    }

    private fun performLoginRequest(postLoginRequest: POSTLoginRequest, navController: NavController) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.performLoginRequest(postLoginRequest) { response: Response<POSTLoginResponse> ->
                handleLoginResponse(response, navController)
            }
        }
    }

    private fun handleLoginResponse(
        response: Response<POSTLoginResponse>,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (response.isSuccessful) {
                val setCookieHeader = response.headers().values("Set-Cookie").firstOrNull()
                val sessionId = setCookieHeader?.substringAfter("JSESSIONID=")?.substringBefore(";")
                if (sessionId != null) {
                    AirbankApplication.prefs.setString("JSESSIONID", sessionId)
                }
                Log.d(TAG, "Session ID: ${AirbankApplication.prefs.getString("JSESSIONID", "")}")
                val loginResponse = response.body()
                if (loginResponse != null) {
                    val name = loginResponse.data.name
                    val phoneNumber = loginResponse.data.phoneNumber

                    if (name.isNullOrEmpty() || phoneNumber.isNullOrEmpty()) {
                        // Either name or phoneNumber is empty, navigate to the signup page
                        Log.d(TAG, "name: $name")
                        Log.d(TAG, "number: $phoneNumber")
                        // Implement navigation to signup screen
                        withContext(Dispatchers.Main) {
                            navController.navigate(BottomNavItem.SignUp.screenRoute)
                        }
                    } else {
                        // Both name and phoneNumber are not empty, navigate to the main screen
                        // Implement navigation to main screen
                        repository.getUserInfo()
                        withContext(Dispatchers.Main) {
                            navController.navigate(BottomNavItem.Main.screenRoute)
                        }
                    }
                }
            } else {
                // Handle the response status other than success
                Log.e(TAG, "Login failed with HTTP status code: ${response.code()}")
            }
        }
    }
}