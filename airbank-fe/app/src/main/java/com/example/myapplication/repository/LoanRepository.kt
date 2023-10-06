package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.api.ApiService
import com.example.myapplication.model.LoanChargeRequest
import com.example.myapplication.model.LoanChargeResponse
import com.example.myapplication.model.LoanRepaymentRequest
import com.example.myapplication.model.LoanRepaymentResponse
import com.example.myapplication.model.LoanResponse
import com.example.myapplication.model.LoanStartRequest
import com.example.myapplication.model.LoanStartResponse
import com.example.myapplication.model.Resource
import com.example.myapplication.model.State
import retrofit2.Response
import javax.inject.Inject

class LoanRepository @Inject constructor(
    private val apiService: ApiService
){



    suspend fun getLoan(groupId: Int): Resource<LoanResponse> {
        val response = apiService.getLoan(groupId)
        return if (response.isSuccessful) {
            Resource(State.SUCCESS, response.body(), "SUCCESS")
        } else {
            Resource(State.ERROR, null, "ERROR")
        }
    }


    suspend fun loanStart(request: LoanStartRequest): Resource<LoanStartResponse>{
        val response = apiService.loanStart(request)
        return if (response.isSuccessful){
            Resource(State.SUCCESS,response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR,null,"ERROR")
        }
    }

    suspend fun loanRepayment(request: LoanRepaymentRequest): Resource<LoanRepaymentResponse>{
        val response = apiService.loanRepayment(request)
        return if (response.isSuccessful){
            Resource(State.SUCCESS,response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR,null,"ERROR")
        }
    }

    suspend fun loanCharge(groupId: Int, request: LoanChargeRequest): Resource<LoanChargeResponse>{
        val response = apiService.loanCharge(groupId,request)
        return if (response.isSuccessful){
            Resource(State.SUCCESS,response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR,null,"ERROR")
        }
    }






}