package com.example.myapplication.screens


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.AirbankApplication
import com.example.myapplication.R
import com.example.myapplication.model.CreateSavingsItemRequest
import com.example.myapplication.viewmodel.SavingsViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

//import com.google.accompanist.coil.rememberImagePainter

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable
fun ChildSavingsApplication(navController: NavController) {
    val viewModel : SavingsViewModel = hiltViewModel()
    val savingsData by viewModel.savingsState.collectAsState(initial = null)
    val groupId = AirbankApplication.prefs.getString("group_id", "")

    var targetValue by remember { mutableStateOf(TextFieldValue()) }
    var priceValue by remember { mutableStateOf(TextFieldValue()) }
    var requestPriceValue by remember { mutableStateOf(TextFieldValue()) }
    var uri by remember { mutableStateOf<Uri?>(null) }
    var selectedMonths by remember { mutableStateOf(3) }
    val calendar: Calendar = Calendar.getInstance()
    var today by remember { mutableStateOf("") }
    today =
        "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    val context = LocalContext.current
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current
    val createItemState by viewModel.createItemState.collectAsState()


    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    if (showDatePicker) {
        val datePickerDialog =
            DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                showDatePicker = false
            }, year, month, day)
        datePickerDialog.show()
        showDatePicker = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())

    ) {
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.arrowleft),
//                contentDescription = "Back",
//                modifier = Modifier
//                    .size(24.dp)
//                    .clickable {
//                        navController.navigate("childSavings")
//                    }
//            )
//
//            Text(
//                "티끌 신청하기",
//                fontSize = 20.sp
//            )
//
//
//
//        }
//
        Spacer(modifier = Modifier.height(16.dp))

        if (snackbarMessage != null) {
            Snackbar(
                modifier = Modifier
                    .padding(16.dp),

                action = {
                    TextButton(onClick = { snackbarMessage = null }) {
                        Text(text = "닫기")
                    }
                }
            ) {
                Text(text = snackbarMessage!!)
            }
        }

        Text("목표")
        val targetMaxLength = 10
        TextField(
            value = targetValue,
            onValueChange = { newValue ->
                if (newValue.text.length <= targetMaxLength) {
                    targetValue = newValue
                }
            },
            label = { Text("목표를 입력하세요.") },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFD6F2FF)),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),


            )

        Spacer(modifier = Modifier.height(20.dp))

        Text("가격")

        TextField(
            value = priceValue,
            onValueChange = { newValue ->
                priceValue = newValue
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFD6F2FF)),
            label = { Text("가격을 입력하세요.") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("요청 금액")

        TextField(
            value = requestPriceValue,
            onValueChange = { newValue ->
                requestPriceValue = newValue
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFD6F2FF)),
            label = { Text("요청 가격을 입력하세요.") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(20.dp))


        var selectedMonths by remember { mutableStateOf(1) }
        var expanded by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()
        val futureCalendar: Calendar = Calendar.getInstance()
        futureCalendar.add(Calendar.MONTH, selectedMonths)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        selectedDate = sdf.format(futureCalendar.time)

        Text("기간: $today ~ $selectedDate")

        Box(
            modifier = Modifier.fillMaxWidth(),

        ) {
            Button(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("기간 선택 : $selectedMonths 개월")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                (1..12).forEach { num ->
                    DropdownMenuItem(
                        text = { Text("$num") },
                        onClick = {
                            selectedMonths = num
                            expanded = false
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(20.dp))



        val singlePhotoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {
                uri = it
            }
        )

        if(uri == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(3.dp, Color(0xFF00D2F3), RoundedCornerShape(10.dp))
                    .padding(horizontal = 20.dp)
                    .background(color = Color.White)
                    .clickable(
                        onClick = {
                            singlePhotoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Spacer(modifier = Modifier.size(15.dp))
                    Image(
                        painter = painterResource(id = R.drawable.imagepicker),
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        "첨부하기",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00D2F3)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(color = Color.White)
                    .clip(RoundedCornerShape(10.dp))
                    .border(3.dp, Color(0xFF00D2F3), RoundedCornerShape(10.dp))
                    .padding(horizontal = 20.dp)
                    .clickable(
                        onClick = {
                            singlePhotoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
            ){
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.size(500.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color(0xFF00D2F3))
                .clickable {
                    when {
                        targetValue.text.isEmpty() -> Toast
                            .makeText(context, "목표를 입력하세요.", Toast.LENGTH_SHORT)
                            .show()

                        priceValue.text.isEmpty() -> Toast
                            .makeText(context, "가격을 입력하세요.", Toast.LENGTH_SHORT)
                            .show()

                        requestPriceValue.text.isEmpty() -> Toast
                            .makeText(context, "요청 금액을 입력하세요.", Toast.LENGTH_SHORT)
                            .show()

                        uri == null -> Toast
                            .makeText(context, "첨부파일을 추가하세요.", Toast.LENGTH_SHORT)
                            .show()

                        selectedMonths <= 0 -> Toast
                            .makeText(context, "기간을 선택하세요.", Toast.LENGTH_SHORT)
                            .show()

                        else -> {
                            val createSavingsItemRequest = CreateSavingsItemRequest(
                                name = targetValue.text,
                                amount = priceValue.text.toIntOrNull() ?: 0,
                                month = selectedMonths,
                                parentsAmount = requestPriceValue.text.toIntOrNull() ?: 0,
                                imageUrl = uri.toString()
                            )
                            Log.d("이미지", "${uri}")

                            viewModel.createSavingsItem(createSavingsItemRequest)
                            Log.d(
                                "CreateItem",
                                "티클 모으기: Sending Item: $createSavingsItemRequest, 그룹id: ${groupId}"
                            )

                            AlertDialog
                                .Builder(context)
                                .setTitle("티끌 모으기")
                                .setMessage("티끌 모으기 신청이 완료되었습니다.")
                                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                                    dialog.dismiss()
                                    navController.navigate("childSavings")
                                }
                                .show()
                        }
                    }
                }
        ) {
            Text(
                "신청하기",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

