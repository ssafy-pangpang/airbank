package com.example.myapplication.screens

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.horizontalScroll

import androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.AirbankApplication
import com.example.myapplication.model.GETCreditHistoryResponse
import com.example.myapplication.network.HDRetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@Composable
fun CreditScoreBox() {

    val viewModel : CreditScoreViewModel = viewModel()
    LaunchedEffect(Unit){
        val groupid =  AirbankApplication.prefs.getString("group_id","")
        if (groupid.isNotEmpty()){
            viewModel.getCreditHistory(groupid.toInt())
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()

    ) {

//        val data = listOf(
//            Pair("2023-08-04T10:44:01.680342", 0f),
//            Pair("2023-08-05T12:44:01.680342", 720f),
//            Pair("2023-08-06T12:44:01.680342", 820f),
//            Pair("2023-08-07T12:44:01.680342", 920f),
//            Pair("2023-08-10T12:44:01.680342", 620f),
//            Pair("2023-08-14T19:44:01.680342", 715f),
//            // Add more data points as needed
//        )
        Text(text = "신용점수 변화 그래프")
        PerformanceChart2(viewModel.creditHistories)
    }
}



@Composable
fun PerformanceChart2(data: List<GETCreditHistoryResponse.Data.creditHistory>) {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())

    val parsedData = data.map { (creditScore, createdAt) ->
        val date = simpleDateFormat.parse(createdAt) ?: Date()
        Pair(date, creditScore + 0f)
    }
    Log.d("Chart", parsedData.toString())

    val minDate = parsedData.minByOrNull { it.first }?.first ?: Date()
    val maxDate = parsedData.maxByOrNull { it.first }?.first ?: Date()
    val minValue = 0f
    val maxValue = 1000f

    val yStep = 50f
    val xStep = ((maxDate.time - minDate.time) / (1000 * 24 * 60 * 60)).toFloat()

    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    val scrollState = rememberScrollState(0)
    Box(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .fillMaxSize()
    ){
        Canvas(
            modifier = Modifier
                .width((xStep * parsedData.size * yStep).dp)  // 각 데이터 포인트마다 40dp의 간격을 둔다고 가정
                .height(1000.dp)
                .padding(16.dp)
        ) {

            // x-축과 y-축의 시작점을 계산합니다
            val xAxisStart = Offset(x = scrollState.value.toFloat(), y = size.height)
            val yAxisStart = Offset(x = scrollState.value.toFloat(), y = size.height)

            // x-축과 y-축을 그립니다
            drawLine(color = Color.Black, start = xAxisStart, end = Offset(size.width, size.height), strokeWidth = 2f)
            drawLine(color = Color.Black, start = yAxisStart, end = Offset(scrollState.value.toFloat(), size.height - yStep*5), strokeWidth = 2f)

            var previousX = 0f
            var previousY = 0f

            for (i in 1 until 6) {
                val yValue = size.height - (i * yStep)
                Log.d("Chart", "yValue=$yValue")

                drawContext.canvas.nativeCanvas.drawText(
                    "${(i) * 200}",
                    scrollState.value + 40f,
                    yValue - 5f,
                    textPaint
                )
            }

            parsedData.forEachIndexed { index, (date, value) ->
                val x = ((date.time - minDate.time) / (1000 * 24 * 60 * 60)).toFloat() * 200f + 100f
                val y = size.height  - value - 100f

                if (x > scrollState.value){

                    if (index > 0) {
                        drawLine(
                            Color(0xff5FCFEF),
                            start = Offset(previousX, previousY),
                            end = Offset(x, y),
                            strokeWidth = 5f
                        )
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        SimpleDateFormat("MM/dd", Locale.getDefault()).format(date),
                        x,
                        size.height - 30,
                        textPaint
                    )
                }
                previousX = x
                previousY = y
            }
        }

    }
}


class CreditScoreViewModel @Inject constructor() : ViewModel() {

    var creditHistories: List<GETCreditHistoryResponse.Data.creditHistory> = emptyList()
    fun getCreditHistory(groupid: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val response = HDRetrofitBuilder.HDapiService().getCreditHistory(groupid)
            if (response.isSuccessful){
                val getresponse = response.body()
                if(getresponse != null){
                    creditHistories = getresponse.data.creditHistories
                }
            }
        }
    }
}