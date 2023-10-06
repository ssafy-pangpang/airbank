package com.example.myapplication.screens

import android.util.Log
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.AirbankApplication
import com.example.myapplication.R
import com.example.myapplication.model.GETGroupsResponse
import com.example.myapplication.network.HDRetrofitBuilder
import com.example.myapplication.viewmodel.SavingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel = viewModel()

    var childs by remember { mutableStateOf<List<GETGroupsResponse.Data.Member>>(emptyList()) }
    LaunchedEffect(Unit, viewModel.childs){
        viewModel.getGroup()
        val mutablechilds = viewModel.childs ?: emptyList()
        childs = mutablechilds
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (childs.isNotEmpty()){
            Row(
                modifier = Modifier
                    .padding(16.dp)
            ) {
            Text("관리중인 자녀 ", style = TextStyle(fontFamily = FontFamily(Font(R.font.pretendardregular)) ))
            Text(childs.size.toString(), style = TextStyle(fontFamily = FontFamily(Font(R.font.pretendardregular))), color = Color(0xFF00D2F3) )
            }
            ChildProfile(childs,viewModel,navController)
        } else {
            postNewChild(navController)
        }
        Body(navController = navController)
    }
}
@Composable
fun postNewChild(navController: NavController){
    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFD6F2FF))
            .width(351.dp)
            .height(133.dp)
            .clickable { navController.navigate("addChild") }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(size = 50.dp))
                    .background(Color(0xFFF4F4F4))
            ){
                Image(
                    painter = painterResource(id = R.drawable.plus_color),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(Modifier.width(16.dp))
            Text("자녀를\n등록해주세요.")
        }
    }
}

@Composable
fun ChildProfile(childs: List<GETGroupsResponse.Data.Member>, viewModel: MainViewModel, navController: NavController) {


    Row (
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        childs.forEach() { child ->

            Column {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(31.5.dp))
                        .border(
                            1.dp,
                            color = if (child == viewModel.selected) Color(0xFFB4EBF7) else Color.Transparent,
                            CircleShape
                        )
                        .clickable {
                            viewModel.selected = child
                        }
                ) {
                    if(child.imageUrl != null){
                        AsyncImage(
                            model = child.imageUrl,
                            contentDescription = "Main Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }else{
                        Image(
                            painter = painterResource(id = R.drawable.karina4),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier= Modifier.fillMaxSize()
                        )
                    }
                }
                Text(child.name)
            }
            Spacer(modifier = Modifier.size(20.dp))
        }
        Column {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(31.5.dp))
                    .border(0.1.dp, Color(0xFF3B3D3D), CircleShape)
                    .clickable {
                        navController.navigate("addChild")
                    }
            ) {
                Image(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(text = "")
        }
    }
    ChildCard(viewModel)
}
@Composable
fun ChildCard(viewModel: MainViewModel) {
    var selectChild by remember { mutableStateOf(GETGroupsResponse.Data.Member(0,0,"","",0)) }
    selectChild = viewModel.selected ?: viewModel.childs.firstOrNull() ?: GETGroupsResponse.Data.Member(0,0,"","",0)
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFD6F2FF))

    ){
        Column (
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(selectChild?.imageUrl.isNullOrEmpty()){
                    Image(
                        painter = painterResource(R.drawable.karina),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }else{
                    AsyncImage(
                        model = selectChild?.imageUrl ?: "",
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(Modifier.width(16.dp))
                Text("자녀 ${selectChild?.name ?: ""} 님의\n지갑을 관리하고 있습니다.")
            }
            Text("신용점수")
//            val creditPoint = AirbankApplication.prefs.getString("creditScore", "")
            val creditPoint = selectChild?.creditScore ?: 0
            ScoreBar(creditPoint)
        }
    }
    Spacer(modifier = Modifier.size(20.dp))
}

@Composable
fun ScoreBar(score: Int) {
    val maxScore = 1000
    val scoreWidth = (score.toFloat() / maxScore.toFloat())

    Row (
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
//                .padding(horizontal = 16.dp)
    ){
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .weight(1f)
                .height(15.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Gray)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(scoreWidth)
                    .height(15.dp)
                    .background(Color(0xFF00D2F3))
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        Spacer( modifier = Modifier.size(10.dp) )

        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF00D2F3),fontSize = 22.sp ,fontWeight = FontWeight.Bold)) {
                    append("$score")
                }
                append("/1000 P")
            }
        )
    }
}

@Composable
fun Body(navController: NavController) {
    val savingsViewModel: SavingsViewModel = hiltViewModel()
    val savingsData by savingsViewModel.savingsState.collectAsState(initial = null)

    LaunchedEffect(key1 = null) {
        savingsViewModel.getSavings()
    }
    Log.d("티끌 상태","${savingsData?.data?.data?.status}요")
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(300.dp)
    ){
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = 16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color(0xFFFFE9E8))
                .clickable {
                    navController.navigate("wallet")
                }
        ){
            Column (
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp)

            ){
                Text(
                    "용돈\n사용 현황",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    "이번 달에는\n얼마를 썼을까?",
                    fontSize = 13.sp,
                    lineHeight = 17.sp
                )

            }
            Image(
                painter = painterResource(id = R.drawable.wallet),
                contentDescription = null,
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.BottomEnd)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Color(0xFFE2ECFF))
                    .clickable {
                        navController.navigate("Loan")
                    }
            ){
                Column (
                    modifier = Modifier
                        .padding(top=10.dp, start = 10.dp)
                ){
                    Text(
                        "땡겨쓰기\n현황",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        "빌려간 돈\n확인하기",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                    )

                }

                Image(
                    painter = painterResource(id = R.drawable.loan),
                    contentDescription = null,
                    modifier = Modifier
                        .size(130.dp)
                        .offset(x = 18.dp)
                        .align(Alignment.BottomEnd)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Color(0xFFEFE4FF))
                    .clickable {
                        if (savingsData?.data?.data?.status == null) {
                            navController.navigate("SavingsWaiting")
                        } else if (savingsData?.data?.data?.status == "PENDING") {
                            navController.navigate("savingsApprove")
                        } else if (savingsData?.data?.data?.status == "PROCEEDING") {
                            navController.navigate("savings")
                        }
                    }
            ){
                Column (
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                ){
                    Text(
                        "티끌 모으기\n현황",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        "저금 현황\n확인하기",
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.savings),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}


class MainViewModel @Inject constructor() : ViewModel() {

    var selected: GETGroupsResponse.Data.Member? by mutableStateOf(null)
    var childs : List<GETGroupsResponse.Data.Member> = emptyList()


    fun getGroup() {
        val tag = "getGroup"
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = HDRetrofitBuilder.HDapiService().getGroups()
                if (response.body() != null) {
                    val getGroupsResponse = response.body()!!.data
                    childs = getGroupsResponse.members
                    if (childs.isNullOrEmpty()){Log.e("MainViewModel","Childs are empty")}
                    else{
                        AirbankApplication.prefs.setString("group_id",childs.first().groupId.toString())
                        Log.d(tag,childs.first().groupId.toString())
                        GETGroupseFund(childs.first().groupId)
                    }
                } else { Log.e("MainViewModel", "Response not successful: ${response.code()}") }
            } catch (e: Exception) { Log.e("MainViewModel", "Error: ${e.message}")  }
        }
    }

    fun GETGroupseFund(group_id: Int){
        if (group_id == 0){return}
        val tag = "ChildRule"
        viewModelScope.launch(Dispatchers.IO) {
            val response = HDRetrofitBuilder.HDapiService().getGroupsFund(group_id)
            val getresponse = response.body()
            if (getresponse != null){
                if(getresponse.data != null ){
                    AirbankApplication.prefs.setString("allowanceAmount",getresponse.data.allowanceAmount.toString())
                    AirbankApplication.prefs.setString("allowanceDate",getresponse.data.allowanceDate.toString())
                    AirbankApplication.prefs.setString("taxRate",getresponse.data.taxRate.toString())
                    AirbankApplication.prefs.setString("confiscationRate",getresponse.data.confiscationRate.toString())
                    AirbankApplication.prefs.setString("loanLimit",getresponse.data.loanLimit.toString())
                }
            }
        }
    }
}

