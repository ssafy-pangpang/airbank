package com.example.myapplication.screens

import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.AirbankApplication
import com.example.myapplication.model.LoanStartRequest
import com.example.myapplication.viewmodel.AccountViewModel
import com.example.myapplication.viewmodel.LoanViewModel
import com.example.myapplication.viewmodel.SavingsViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ChildLoanStartScreen(navController: NavController) {
    val viewModel: LoanViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            LoanAmount(viewModel)
            Spacer(modifier = Modifier.padding(10.dp))
            LoanDetail()
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
                .background(Color.Transparent)
        )
        LoanButton(navController, viewModel)
    }
}

@Composable
fun CreditScore() {

    Column(
    ) {
        val creditPoint = AirbankApplication.prefs.getString("creditScore", "")
        Text(

            "신용점수: ${creditPoint}",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun LoanAmount(viewModel: LoanViewModel) {
    var requestPriceValue by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(135.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color(0xffD6F2FF))
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(13.dp)
        ) {
            Text(
                "땡겨쓸 금액",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.size(10.dp))
            TextField(
                value = requestPriceValue,
                onValueChange = { newValue ->
                    requestPriceValue = newValue
                    viewModel.setLoanAmount(requestPriceValue)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .background(color = Color(0xFFD6F2FF)),
                label = { Text("땡겨쓸 금액을 입력 하세요.") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
            )
        }
    }
}

@Composable
fun LoanButton(navController: NavController, viewModel: LoanViewModel) {
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color(0xFF00D2F3))
            .clickable {
                if (viewModel.loanAmount.value.text.isDigitsOnly() && viewModel.loanAmount.value.text != "") {
                    viewModel.loanStart()
                    AlertDialog
                        .Builder(context)
                        .setTitle("땡겨쓰기")
                        .setMessage("땡겨쓰기가 완료되었습니다.")
                        .setPositiveButton(android.R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                            navController.navigate("ChildLoan")
                        }.show()
                } else {
                    Toast.makeText(context,"금액을 숫자로 입력해 주세요.",Toast.LENGTH_SHORT).show()
                }
            }
    ) {
        Text(
            "땡겨쓰기",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoanDetail() {
    var groupId by remember { mutableStateOf("") }
    groupId = AirbankApplication.prefs.getString("group_id", "")
    val loanViewModel: LoanViewModel = hiltViewModel()
    val loanData by loanViewModel.loanState.collectAsState()
    val accountViewModel: AccountViewModel = hiltViewModel()
    val interestData by accountViewModel.interestCheckState.collectAsState(initial = null)

    LaunchedEffect(key1 = groupId) {
        if (groupId.isNotEmpty()) {
            Log.d("ChildLoanStart", "LaunchedEffect triggered with groupId: $groupId")
            accountViewModel.interestCheck(groupId.toInt())
        }
    }

    Column() {
        Text(
            "변동사항",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        loanData?.let { data ->
            val loanLimit = data.data?.data?.loanLimit ?: 0
            val amount = data.data?.data?.amount ?: 0
            val loanRemaining = loanLimit - amount ?:0
            val interestful = (interestData?.data?.data?.amount ?:0) + (interestData?.data?.data?.overdueAmount ?:0)

            val formattedLoanRemaining = NumberFormat.getNumberInstance(Locale.US).format(loanRemaining)
            val formattedLoamLimit = NumberFormat.getNumberInstance(Locale.US).format(loanLimit)
            val formattedAmount = NumberFormat.getNumberInstance(Locale.US).format(amount)
            val formattedInterestful = NumberFormat.getNumberInstance(Locale.US).format(interestful)


            Column {
                Text(
                    "한도 금액: ${formattedLoamLimit}원",
                    fontSize = 14.sp,
                    color = Color(0xff515151)
                )
                Text(
                    "사용 금액: ${formattedAmount}원",
                    fontSize = 14.sp,
                    color = Color(0xff515151)
                )
                Text(
                    "땡겨쓰기 가능 금액: ${formattedLoanRemaining}원",
                    fontSize = 14.sp,
                    color = Color(0xff515151)
                )
                Text(
                    "이자: ${formattedInterestful}원",
                    fontSize = 14.sp,
                    color = Color(0xff515151)
                )
            }
        }
    }
}

