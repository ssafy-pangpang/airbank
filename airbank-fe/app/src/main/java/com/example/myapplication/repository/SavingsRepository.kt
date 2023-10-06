package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.api.ApiService
import com.example.myapplication.model.BonusSavingsRequest
import com.example.myapplication.model.BonusSavingsResponse
import com.example.myapplication.model.CancelSavingsRequest
import com.example.myapplication.model.CancelSavingsResponse
import com.example.myapplication.model.CreateSavingsItemRequest
import com.example.myapplication.model.CreateSavingsItemResponse
import com.example.myapplication.model.NotificationResponse
import com.example.myapplication.model.Resource
import com.example.myapplication.model.SavingsRemitRequest
import com.example.myapplication.model.SavingsRemitResponse
import com.example.myapplication.model.SavingsResponse
import com.example.myapplication.model.State
import com.example.myapplication.model.UpdateSavingsRequest
import com.example.myapplication.model.UpdateSavingsResponse
import dagger.hilt.android.HiltAndroidApp
import okhttp3.MultipartBody
import javax.inject.Inject


class SavingsRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getSavings(groupId: Int): Resource<SavingsResponse> {
        val response = apiService.getSavings(groupId)
        return if (response.isSuccessful) {
            Resource(State.SUCCESS, response.body(),"SUCCESS")
        } else{
            Resource(State.ERROR, null, "ERROR")
        }
    }

    suspend fun createSavingsItem(request: CreateSavingsItemRequest): Resource<CreateSavingsItemResponse> {
        val response = apiService.createSavingsItem(request)
        Log.d("CreateItem", "티클 ${response.code()}")
        Log.d("CreateItem", "티클 ${response.message()}")
        return if (response.isSuccessful) {
            Resource(State.SUCCESS, response.body(), "SUCCESS !")
        } else {
            Resource(State.ERROR, null, "ERROR !")
        }
    }

    suspend fun updateSavings(groupId: Int, request: UpdateSavingsRequest): Resource<UpdateSavingsResponse> {
        val response = apiService.updateSavings(groupId, request)
        return if (response.isSuccessful) {
            Resource(State.SUCCESS, response.body(), "SUCCESS !")
        } else {
            Resource(State.ERROR, null, "ERROR !")
        }
    }

    suspend fun cancelSavings(request: CancelSavingsRequest): Resource<CancelSavingsResponse> {
        val response = apiService.cancelSavings(request)
        Log.d("CancelItem", "티끌캔슬c ${response.code()}")
        Log.d("CancelItem", "티끌캔슬m ${response.message()}")
        return if (response.isSuccessful) {
            Resource(State.SUCCESS, response.body(), "SUCCESS !")
        } else {
            Resource(State.ERROR, null, "ERROR !")
        }
    }

    suspend fun remitSavings(request: SavingsRemitRequest): Resource<SavingsRemitResponse> {
        val response = apiService.remitSavings(request)
        return if (response.isSuccessful) {
            Resource(State.SUCCESS, response.body(), "SUCCESS !")
        } else {
            Resource(State.ERROR, null, "ERROR !")
        }
    }

    suspend fun bonusSavings(groupId: Int, request: BonusSavingsRequest): Resource<BonusSavingsResponse> {
        val response = apiService.bonusSavings(groupId, request)
        return if (response.isSuccessful) {
            Resource(State.SUCCESS, response.body(), "SUCCESS !")
        } else {
            Resource(State.ERROR, null, "ERROR !")
        }
    }

    suspend fun getNotifications(groupId: Int): Resource<NotificationResponse> {
        val response = apiService.getNotifications(groupId)
        Log.d("알림", "알림코드 ${response.code()}")
        Log.d("알림", "알림메세지 ${response.message()}")
        return if (response.isSuccessful) {
            Resource(State.SUCCESS, response.body(),"SUCCESS!")
        } else{
            Resource(State.ERROR, null, "ERROR!")
        }
    }
}
