package com.example.myapplication.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.AirbankApplication
import com.example.myapplication.model.PATCHGroupsConfirmRequest
import com.example.myapplication.network.HDRetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@Composable
fun GroupConfirmScreen( navController: NavController
) {
    val viewModel: GroupConfirmViewModel = viewModel()
    val group_id: Int = AirbankApplication.prefs.getString("tempid","0").toInt()
    AirbankApplication.prefs.setString("tempid","")
    LaunchedEffect(Unit){
        viewModel.getgroupsfund(group_id)
    }

    var taxRate by remember { mutableStateOf("") }
    var allowanceAmount by remember { mutableStateOf("") }
    var allowanceDate by remember { mutableStateOf("") }
    var confiscationRate by remember { mutableStateOf("") }
    var loanLimit by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp, 16.dp)
            .fillMaxSize()
    ) {
        Row() {
            OutlinedTextField(
                value = allowanceAmount,
                onValueChange = { allowanceAmount = it },
                label = { Text(text = "용돈: ${viewModel.allowanceAmount}원") },
                modifier = Modifier
                    .widthIn(max = 150.dp)
                    .weight(1f),
                enabled = false
            )
        }
        Row() {
            OutlinedTextField(
                value = allowanceDate,
                onValueChange = { allowanceDate = it },
                label = { Text(text = "용돈 지급일: ${viewModel.allowanceDate}일") },
                modifier = Modifier
                    .widthIn(max = 150.dp)
                    .weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number // 숫자 키패드를 보이도록 설정
                ),
                enabled = false
            )
            Spacer(modifier = Modifier.width(20.dp))
            OutlinedTextField(
                value = taxRate,
                onValueChange = { taxRate = it },
                label = { Text(text = "세율: ${viewModel.taxRate}%") },
                modifier = Modifier
                    .widthIn(max = 150.dp)
                    .weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number // 숫자 키패드를 보이도록 설정
                ),
                enabled = false
            )
        }
        Row() {
            OutlinedTextField(
                value = loanLimit,
                onValueChange = { loanLimit = it },
                label = { Text(text = "땡겨쓰기 한도: ${viewModel.loanLimit}원") },
                modifier = Modifier
                    .widthIn(max = 150.dp)
                    .weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number // 숫자 키패드를 보이도록 설정
                ),
                enabled = false
            )
            Spacer(modifier = Modifier.width(20.dp))
            OutlinedTextField(
                value = confiscationRate,
                onValueChange = { confiscationRate = it },
                label = { Text(text = "압류: ${viewModel.confiscationRate}%") },
                modifier = Modifier
                    .widthIn(max = 150.dp)
                    .weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number // 숫자 키패드를 보이도록 설정
                ),
                enabled = false
            )
        }
        if(AirbankApplication.prefs.getString("tempid","").isNotEmpty()){
            Row(){
                Button(
                    onClick = {
                        val fundRequest = PATCHGroupsConfirmRequest(
                            isAccept = true
                        )
                        val group_id = AirbankApplication.prefs.getString("group_id","").toInt()
                        if (group_id != 0){
                            viewModel.handlesubmit(navController,fundRequest,group_id)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .weight(1f)
                ) {
                    Text("수락")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = {
                        val fundRequest = PATCHGroupsConfirmRequest(
                            isAccept = false
                        )
                        if (group_id != 0){
                            viewModel.handlesubmit(navController,fundRequest,group_id)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .weight(1f)
                ) {
                    Text("거절")
                }
            }
        }
    }
}

class GroupConfirmViewModel @Inject constructor() : ViewModel(){
   private val tag = "GroupConfirm"

    var taxRate by mutableIntStateOf(0)
    var allowanceAmount by mutableIntStateOf(0)
    var allowanceDate by mutableIntStateOf(0)
    var confiscationRate by mutableIntStateOf(0)
    var loanLimit by mutableIntStateOf(0)


    fun getgroupsfund(group_id: Int){
        Log.d(tag,"group_id: "+group_id.toString())
        viewModelScope.launch(Dispatchers.IO) {
            val response = HDRetrofitBuilder.HDapiService().getGroupsFund(group_id)
            if(response.isSuccessful){
                val getgroupconfirmresponse = response.body()
                if(getgroupconfirmresponse != null){
                    taxRate = getgroupconfirmresponse.data.taxRate
                    allowanceAmount = getgroupconfirmresponse.data.allowanceAmount
                    allowanceDate = getgroupconfirmresponse.data.allowanceDate
                    confiscationRate = getgroupconfirmresponse.data.confiscationRate
                    loanLimit = getgroupconfirmresponse.data.confiscationRate
                    Log.d(tag,getgroupconfirmresponse.toString())
                }
            }
        }
    }
    fun handlesubmit(navController: NavController,patchGroupsConfirmRequest: PATCHGroupsConfirmRequest, group_id: Int){
        Log.d(tag,"group_id: "+group_id.toString())
        viewModelScope.launch(Dispatchers.IO) {
            val response = HDRetrofitBuilder.HDapiService().patchGroupsConfirm(patchGroupsConfirmRequest,group_id)
            if(response.isSuccessful){
                Log.d(tag,response.toString())
                withContext(Dispatchers.Main){
                    navController.popBackStack()
                }
            }else{
                Log.e(tag,"submit failed")
            }
        }
    }
}
