package com.example.myapplication.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.AirbankApplication
import com.example.myapplication.R
import com.example.myapplication.viewmodel.SavingsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun NotificationScreen(navController: NavController) {
    val viewModel: SavingsViewModel = hiltViewModel()
    val notificationData by viewModel.getNotificationsState.collectAsState(initial = null)
    Log.d("알림페이지","${notificationData?.data?.data?.notificationElements}")
    val groupId = AirbankApplication.prefs.getString("group_id", "")

    LaunchedEffect(key1 = groupId){
        if(groupId.isNotEmpty()) {
            viewModel.getNotifications(groupId.toInt())
        }
    }

    notificationData?.data?.data?.notificationElements?.let { notifications ->


        if (notifications.isNotEmpty()) {
            val groupedByDate = notifications.groupBy { it.createdAt.toString().substring(0, 10) }
            val sortedDates = groupedByDate.keys.sortedDescending()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                for (date in sortedDates) {
                    val dailyNotifications = groupedByDate[date] ?: continue
                    val sortedNotifications = dailyNotifications.sortedByDescending { it.createdAt }

                    Text(
                        text = date,
                        modifier = Modifier.padding(8.dp),
                        style = TextStyle(color = Color.Black)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        for (notification in sortedNotifications) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White)
                                    .clickable {
                                        if (notification.notificationType == "GROUP_CONFIRM") {
                                            navController.navigate("GroupConfirm")
                                        } else if (notification.notificationType == "TAX") {
                                            navController.navigate("ChildWallet")
                                        } else if (notification.notificationType == "LOAN") {
                                            navController.navigate("ChildLoan")
                                        } else if (notification.notificationType == "INTEREST") {
                                            navController.navigate("ChildLoan")
                                        } else if (notification.notificationType == "ALLOWANCE") {
                                            navController.navigate("ChildWallet")
                                        } else if (notification.notificationType == "CONFISCATION") {
                                            navController.navigate("ChildWallet")
                                        } else if (notification.notificationType == "SAVINGS") {
                                            navController.navigate("childSavings")
                                        } else if (notification.notificationType == "SAVINGS_CONFIRM") {
                                            navController.navigate("SavingsScreen")
                                        } else if (notification.notificationType == "SAVINGS_REWARD_CONFIRM") {
                                            navController.navigate("SavingsScreen")
                                        }
                                    }
                                ,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.size(8.dp))
                                Icon(
                                    painter = getImageForNotificationType(notification.notificationType),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = 8.dp)
                                )
                                Column {
                                    Text(
                                        text = when (notification.notificationType){
                                            "TAX" -> "세금"
                                            "INTEREST" -> "이자"
                                            "BONUS" -> "보너스"
                                            "ALLOWANCE" -> "용돈"
                                            "MISSION" -> "미션"
                                            "CONFISCATION" -> "압류"
                                            "LOAN" -> "땡겨 쓰기"
                                            "SAVINGS" -> "티끌 모으기"
                                            "SAVINGS_CONFIRM" -> "티끌 모으기"
                                            "SAVINGS_REWARD_CONFIRM" -> "티끌 모으기"
                                            "GROUP" -> "그룹"
                                            "GROUP_CONFIRM" -> "그룹"
                                            else -> notification.notificationType
                                        },
                                        style = TextStyle(color = Color.Black),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "${notification.content}",
                                        style = TextStyle(color = Color.Black),
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        hoursAgo(notification.createdAt),
                                        style = TextStyle(
                                            color = Color.Gray,
                                            fontSize = 8.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else{
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "알림이 없습니다", style = TextStyle(color = Color.Gray, fontSize = 16.sp))
            }
        }
    }?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "알림이 없습니다", style = TextStyle(color = Color.Gray, fontSize = 16.sp))
        }
    }
}

@Composable
fun getImageForNotificationType(type: String): Painter {
    return when (type) {
        "TAX", "CONFISCATION" -> painterResource(id = R.drawable.tax)
        "INTEREST","LOAN" -> painterResource(id = R.drawable.loancoin)
        "BONUS" -> painterResource(id = R.drawable.pinmoney)
        "ALLOWANCE" -> painterResource(id = R.drawable.pinmoney)
        "SAVINGS", "SAVINGS_CONFIRM", "SAVINGS_REWARD_CONFIRM" -> painterResource(id = R.drawable.savingscoin)
        "GROUP", "GROUP_CONFIRM" -> painterResource(id = R.drawable.group)

        else -> painterResource(id = R.drawable.plus)
    }
}



fun hoursAgo(time: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
    val date: Date? = inputFormat.parse(time)

    if (date != null) {
        val now = Calendar.getInstance()
        val timeEvent = Calendar.getInstance().apply { this.time = date }  // 수정된 부분

        if (now.get(Calendar.DATE) == timeEvent.get(Calendar.DATE)) {
            val diffHours = now.get(Calendar.HOUR_OF_DAY) - timeEvent.get(Calendar.HOUR_OF_DAY)
            if (diffHours < 1) {
                val diffMinutes = now.get(Calendar.MINUTE) - timeEvent.get(Calendar.MINUTE)
                return "$diffMinutes 분 전"
            }
            return "$diffHours 시간 전"
        } else {
            return "${timeEvent.get(Calendar.HOUR_OF_DAY)}시 ${timeEvent.get(Calendar.MINUTE)}분"
        }
    }

    return "알 수 없음"
}

