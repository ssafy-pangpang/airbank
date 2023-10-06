package com.example.myapplication.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import android.net.Uri
import android.net.http.UrlRequest.Status
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.example.myapplication.R
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import coil.compose.AsyncImage
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.AirbankApplication
import com.example.myapplication.viewmodel.AccountViewModel
import com.example.myapplication.viewmodel.LoanViewModel
import com.example.myapplication.viewmodel.SavingsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat
import com.example.myapplication.model.State
@Composable
fun ChildWalletScreen(navController: NavController) {
    val accountViewModel: AccountViewModel = hiltViewModel()
    val accountData by accountViewModel.accountCheckState.collectAsState(initial = null)
    val confiscationCheckState by accountViewModel.confiscationCheckState.collectAsState(initial = null)
    val taxData by accountViewModel.taxCheckState.collectAsState(initial = null)
    val savingsViewModel: SavingsViewModel = hiltViewModel()
    val savingsData by savingsViewModel.savingsState.collectAsState(initial = null)
    val loanViewModel: LoanViewModel = hiltViewModel()
    val loanData by loanViewModel.loanState.collectAsState(initial = null)

    var groupId by remember { mutableStateOf("") }
    groupId = AirbankApplication.prefs.getString("group_id", "")

    LaunchedEffect(key1 = groupId) {
        if (groupId.isNotEmpty()){
            accountViewModel.accountCheck()
            accountViewModel.confiscationCheck(groupId.toInt())
            accountViewModel.taxCheck(groupId.toInt())
            loanViewModel.getLoan()
        }
    }


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color(0xffD6F2FF))
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 13.dp)
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    "나의 잔액",
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = accountData?.data?.data?.amount?.toString() ?: "돈이 엄서ㅠ",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(10.dp))
                val name = AirbankApplication.prefs.getString("name","")
                Text(
                    "팡팡은행 ${name} 님의 통장"
                )
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
//                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color(0xffD6F2FF))
                .clickable {
                    navController.navigate("ChildTransactionHistory")
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.moneysend),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                Text("거래 내역 보기")

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.vector),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                )

            }
        }
        Spacer(modifier = Modifier.size(17.dp))
        Divider(
            color = Color.Black,
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.size(17.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color(0xFFE4EBED))
                .padding(horizontal = 16.dp)
                .clickable {
                    navController.navigate("ChildTaxTransfer")
                }

        ) {
            Column (
                modifier = Modifier
                    .padding(start = 16.dp)
            ){
                Spacer(modifier = Modifier.size(13.dp))
                Text("이번 달 세금")
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    "${taxData?.data?.data?.amount ?:0}원",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color(0xFFE4EBED))
                .padding(horizontal = 16.dp)
                .let { modifier ->
                    when (confiscationCheckState?.status) {
                        State.SUCCESS -> {
                            if (confiscationCheckState?.data?.data?.startedAt != null) {
                                modifier.clickable {
                                    navController.navigate("ChildConfiscationTransfer")
                                }
                            } else {
                                modifier.clickable {
                                    navController.navigate("ChildLoan")
                                }
                            }
                        }
                        else -> modifier
                    }
                }

        ) {
            Column (
                modifier = Modifier
                    .padding(start = 16.dp)
            ){
                Spacer(modifier = Modifier.size(13.dp))
                Text("대출금")
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    "${loanData?.data?.data?.amount ?:0}원",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            when (confiscationCheckState?.status) {
                State.SUCCESS -> {
                    if (confiscationCheckState?.data?.data?.startedAt != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray.copy(alpha = 0.5f))
                        ) {
                            Text(
                                "압류중",
                                color = Color.White,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
                State.ERROR -> {

                }
                else -> Unit
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color(0xFFE4EBED))
                .padding(horizontal = 16.dp)
                .clickable {
                    navController.navigate("childSavings")
                }

        ) {
            Column (
                modifier = Modifier
                    .padding(start = 16.dp)
            ){
                Spacer(modifier = Modifier.size(13.dp))
                Text("티끌 모으기")
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    "${savingsData?.data?.data?.totalAmount ?:0}원",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}