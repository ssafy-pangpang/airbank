package com.example.myapplication.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.AirbankApplication
import com.example.myapplication.network.HDRetrofitBuilder
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Composable
fun MyPageScreen(navController: NavController) {

    val viewModel: MyPageViewModel = viewModel() // Create an instance of AuthViewModel


    var imagepath by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("")}
    var userrole by remember { mutableStateOf("")}
    var userphonenumber by remember { mutableStateOf("")}


    UserApiClient.instance.me { user, _ ->
        if (user!=null){
            imagepath = AirbankApplication.prefs.getString("imageUrl", "")
            username = AirbankApplication.prefs.getString("name", "")
            userrole = AirbankApplication.prefs.getString("role", "")
            userphonenumber = AirbankApplication.prefs.getString("phoneNumber", "")
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize() // Use this modifier to fill the available space
    ) {
        Box(
            modifier = Modifier
                .size(63.dp)
                .clip(RoundedCornerShape(31.5.dp))
                .background(color = Color(0xffd9d9d9).copy(alpha = 0.8f))
        ) {
            // 둥글게 잘린 이미지 추가
            AsyncImage(
                model = imagepath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = username,
                    color = Color(0xff252525),
                    lineHeight = 1.34.em,
                    style = TextStyle(
                        fontSize = 16.sp)
                )
                Text(
                    text = userrole,
                    color = Color(0xff00d2f3),
                    lineHeight = 1.79.em,
                    style = TextStyle(
                        fontSize = 12.sp))
            }
            Text(
                text = userphonenumber,
                color = Color(0xff515151),
                lineHeight = 1.54.em,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium))
        }

        if(userrole == "PARENT")
        {
            Button(
                onClick = { navController.navigate("ChildRule") }, // Use the ViewModel for logout
                modifier = Modifier
                    .widthIn(min = 200.dp)

            ) {
                Text(text = "자녀 규칙 설정" )

            }
        }
        if(userrole == "CHILD")
        {
            Button(
                onClick = { navController.navigate("GroupConfirm") }, // Use the ViewModel for logout
                modifier = Modifier
                    .widthIn(min = 200.dp)

            ) {
                Text(text = "이용 규칙 확인" )

            }
        }
        Button(
            onClick = { performLogout(navController, viewModel) }, // Use the ViewModel for logout
            modifier = Modifier
                .widthIn(min = 200.dp)
        ) {
            Text(text = "로그아웃" )
        }
    }

}

fun performLogout(navController: NavController, viewModel: MyPageViewModel) {
    viewModel.handlelogout(navController)
}



class MyPageViewModel @Inject constructor() : ViewModel() {

    fun handlelogout(navController: NavController){
        val tag = "LOGOUT"
        viewModelScope.launch(Dispatchers.IO) {
            val response = HDRetrofitBuilder.HDapiService().logoutUser()
            Log.d(tag,response.toString())
            if (response.isSuccessful){
                UserApiClient.instance.logout { error -> Log.e(tag+"kakao",error.toString()) }
                AirbankApplication.prefs.setString("JSESSIONID","")
                AirbankApplication.prefs.setString("name", "")
                AirbankApplication.prefs.setString("phoneNumber", "")
                AirbankApplication.prefs.setString("creditScore", "")
                AirbankApplication.prefs.setString("imageUrl", "")
                AirbankApplication.prefs.setString("role", "")
                withContext(Dispatchers.Main){
                    navController.navigate("First")
                }
            }
        }
    }
}