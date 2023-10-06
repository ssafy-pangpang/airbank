package com.example.myapplication.screens

import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.myapplication.viewmodel.LoanViewModel


@Composable
fun ChildLoanRepaymentScreen(navController: NavController) {
    val viewModel: LoanViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            LoanRepaymentAmount(viewModel)
            Spacer(modifier = Modifier.padding(10.dp))
            LoanDetail()
        }
        Spacer(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)
                )
        LoanRepaymentButton(navController, viewModel)

    }
}

@Composable
fun LoanRepaymentButton(navController: NavController, viewModel: LoanViewModel) {
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color(0xFF00D2F3))
            .clickable {
                if (viewModel.loanRepaymentAmount.value.text.isDigitsOnly() && viewModel.loanRepaymentAmount.value.text != "") {
                    viewModel.loanRepayment()
                    AlertDialog
                        .Builder(context)
                        .setTitle("땡겨쓰기")
                        .setMessage("상환이 완료되었습니다.")
                        .setPositiveButton(android.R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                            navController.navigate("ChildLoan")
                        }.show()
                } else {
                    Toast
                        .makeText(context, "금액을 숫자로 입력해 주세요.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    ) {
        Text(
            "상환하기",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoanRepaymentAmount(viewModel: LoanViewModel) {
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
                "상환할 금액",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.size(10.dp))
            TextField(
                value = requestPriceValue,
                onValueChange = { newValue ->
                    requestPriceValue = newValue
                    viewModel.setLoanRepaymentAmount(requestPriceValue)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .background(color = Color(0xFFD6F2FF)),
                label = { Text("상환 금액을 입력 하세요.") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}