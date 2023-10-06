package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.AirbankApplication
import com.example.myapplication.model.PATCHMembersRequest
import com.example.myapplication.model.PATCHMembersResponse
import com.example.myapplication.model.POSTLoginRequest
import com.example.myapplication.model.POSTLoginResponse
import com.example.myapplication.network.HDRetrofitBuilder
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class AuthRepository(private val scope: CoroutineScope) {
    fun retrieveUserInfo( //카카오sdk활용, user.instance(이름,프로필이미지,기본프로필여부)를 반환
        token: OAuthToken,
        onComplete: (POSTLoginRequest) -> Unit
    ) {
        Log.d("Token",token.toString())
        scope.launch(Dispatchers.IO) {
            UserApiClient.instance.me { user, userInfoError ->
                if (userInfoError != null) {
                    // Handle user info request failure here
                } else if (user != null) {
                    val oauthIdentifier = user.id.toString()
                    val profileImageUrl = user.properties?.get("profile_image") ?: ""
                    val isDefaultImageString =
                        user.kakaoAccount?.profile?.isDefaultImage?.toString() ?: ""
                    val isDefaultImage = isDefaultImageString.toBoolean()

                    val postLoginRequest = POSTLoginRequest(
                        oauthIdentifier = oauthIdentifier,
                        imageUrl = profileImageUrl,
                        isDefaultImage = isDefaultImage
                    )

                    onComplete(postLoginRequest)
                }
            }
        }
    }

    fun performLoginRequest( //반환받은 유저 정보를 서버에 보냄
        postLoginRequest: POSTLoginRequest,
        onComplete: (Response<POSTLoginResponse>) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                Log.d("POSTTT",postLoginRequest.toString())
                val response = HDRetrofitBuilder.HDapiService().loginUser(postLoginRequest) //retrofit2활용, airbank서버에 유저 정보 1차 전송
                Log.d("POSTTT",response.toString())

                onComplete(response)
            } catch (e: Exception) {
                // Handle network request exception
                onComplete(Response.error(500, (e.message ?: "Unknown error").toResponseBody(null)))
            }
        }
    }

    fun getUserInfo(){
        scope.launch(Dispatchers.IO) {
            val tag = "USER"
            try {
                val response = HDRetrofitBuilder.HDapiService().getUserInfo()
                val getUserResponse = response.body()
                if(getUserResponse != null){
                    Log.d(tag,"Success "+response.body().toString())
                    AirbankApplication.prefs.setString("name", getUserResponse.data.name)
                    AirbankApplication.prefs.setString("phoneNumber", getUserResponse.data.phoneNumber)
                    AirbankApplication.prefs.setString("creditScore", getUserResponse.data.creditScore.toString())
                    AirbankApplication.prefs.setString("imageUrl", getUserResponse.data.imageUrl ?: "https://thumbnews.nateimg.co.kr/view610///news.nateimg.co.kr/orgImg/nn/2022/06/24/202206241808393510_1.jpg")
                    AirbankApplication.prefs.setString("role", getUserResponse.data.role)
                }else {
                    Log.d(tag,"Fail "+response.body().toString())
                }
            } catch (e:Exception){
                Log.e(tag,e.toString())
            }
        }
    }
}


class SignUpRepository(private val scope: CoroutineScope) {
    private val tag = "SIGNUP"
    fun signUp(
        patchMembersRequest: PATCHMembersRequest,
        onComplete: (Response<PATCHMembersResponse>) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
             try {
                 Log.d(tag,patchMembersRequest.toString())
                val response = HDRetrofitBuilder.HDapiService().signupUser(patchMembersRequest)
                 Log.d(tag,response.toString())

                    onComplete(response)
            } catch (e: Exception) {
                 onComplete(Response.error(500, (e.message ?: "Unknown error").toResponseBody(null)))
            }
        }
    }
}

