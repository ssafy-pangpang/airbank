package com.example.myapplication.repository


import android.util.Log
import com.example.myapplication.api.ApiService
import com.example.myapplication.model.AccountCheckResponse
import com.example.myapplication.model.AccountHistoryCheckResponse
import com.example.myapplication.model.AccountRegisterRequest
import com.example.myapplication.model.AccountRegisterResponse
import com.example.myapplication.model.BonusRequest
import com.example.myapplication.model.BonusResponse
import com.example.myapplication.model.BonusSavingsRequest
import com.example.myapplication.model.BonusSavingsResponse
import com.example.myapplication.model.CancelSavingsRequest
import com.example.myapplication.model.CancelSavingsResponse
import com.example.myapplication.model.ConfiscationResponse
import com.example.myapplication.model.ConfiscationTransferRequest
import com.example.myapplication.model.ConfiscationTransferResponse
import com.example.myapplication.model.CreateSavingsItemRequest
import com.example.myapplication.model.CreateSavingsItemResponse
import com.example.myapplication.model.InterestRepaymentRequest
import com.example.myapplication.model.InterestRepaymentResponse
import com.example.myapplication.model.InterestResponse
import com.example.myapplication.model.Resource
import com.example.myapplication.model.SavingsRemitRequest
import com.example.myapplication.model.SavingsRemitResponse
import com.example.myapplication.model.SavingsResponse
import com.example.myapplication.model.State
import com.example.myapplication.model.TaxResponse
import com.example.myapplication.model.TaxTransferRequest
import com.example.myapplication.model.TaxTransferResponse
import com.example.myapplication.model.UpdateSavingsRequest
import com.example.myapplication.model.UpdateSavingsResponse
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun accountRegister(request: AccountRegisterRequest): Resource<AccountRegisterResponse> {
        val response = apiService.accountRegister(request)
        return if (response.isSuccessful){
            Resource(State.SUCCESS, response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR, null, "ERROR")
        }
    }

    suspend fun accountHistory(accountType: String): Resource<AccountHistoryCheckResponse>{
        val response = apiService.accountHistory(accountType)
        return if (response.isSuccessful){
            Resource(State.SUCCESS, response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR, null, "ERROR")
        }
    }

    suspend fun accountCheck(): Resource<AccountCheckResponse>{
        val response = apiService.accountCheck()
        return if (response.isSuccessful){
            Resource(State.SUCCESS, response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR,null,"ERROR")
        }
    }

    suspend fun taxCheck(groupId: Int): Resource<TaxResponse>{
        val response = apiService.taxCheck(groupId)
        return if (response.isSuccessful){
            Resource(State.SUCCESS,response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR, null, "ERROR")
        }
    }

    suspend fun taxTransfer(request: TaxTransferRequest): Resource<TaxTransferResponse>{
        val response = apiService.taxTransfer(request)
        return if (response.isSuccessful){
            Resource(State.SUCCESS, response.body(),"SUCCESS")
        } else {
            Resource(State.ERROR, null,"ERROR")
        }
    }

    suspend fun bonusTransfer(groupId: Int, request:BonusRequest): Resource<BonusResponse>{
        val response = apiService.bonusTransfer(groupId,request)
        return if (response.isSuccessful){
            Resource(State.SUCCESS, response.body(),"SUCCESS")
        } else {
            Resource(State.ERROR, null,"ERROR")
        }
    }

    suspend fun interestCheck(groupId: Int): Resource<InterestResponse>{
        val response = apiService.interestCheck(groupId)
        return if (response.isSuccessful){
            Resource(State.SUCCESS,response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR,null,"ERROR")
        }
    }

    suspend fun interestRepayment(request: InterestRepaymentRequest): Resource<InterestRepaymentResponse>{
        val response = apiService.interestRepayment(request)
        return if (response.isSuccessful){
            Resource(State.SUCCESS,response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR,null,"ERROR")
        }
    }

    suspend fun confiscationCheck(groupId: Int): Resource<ConfiscationResponse>{
        val response = apiService.confiscationCheck(groupId)
        return if (response.isSuccessful){
            Resource(State.SUCCESS,response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR,null,"ERROR")
        }
    }

    suspend fun confiscationTransfer(request: ConfiscationTransferRequest): Resource<ConfiscationTransferResponse>{
        val response = apiService.confiscationTransfer(request)
        return if (response.isSuccessful){
            Resource(State.SUCCESS,response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR,null,"ERROR")
        }
    }





}
