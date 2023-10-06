package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.AirbankApplication
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
import com.example.myapplication.repository.SavingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
class SavingsViewModel @Inject constructor(
    private val savingsRepository: SavingsRepository
) : ViewModel() {

    private val _savingsState = MutableStateFlow<Resource<SavingsResponse>>(Resource(State.LOADING, null, null))
    val savingsState: StateFlow<Resource<SavingsResponse>> get() = _savingsState

    init {
        getSavings()
    }

    fun getSavings() = viewModelScope.launch {
        _savingsState.emit(Resource(State.LOADING, null, null))
        val groupId = AirbankApplication.prefs.getString("group_id", "")
        try {

            val response = savingsRepository.getSavings(groupId.toInt())
            _savingsState.emit(response)
            Log.d("DEBUG", "_savingsState is set: ${_savingsState.value}, ${groupId}")

        } catch (e: Exception) {
            _savingsState.emit(Resource(State.ERROR, null, e.localizedMessage))
            Log.d("getSavingsError","${groupId}")
        }
    }


    private val _createItemState = MutableStateFlow<Resource<CreateSavingsItemResponse>>(Resource(State.LOADING, null, null))
    val createItemState: StateFlow<Resource<CreateSavingsItemResponse>> get() = _createItemState

    fun createSavingsItem(request: CreateSavingsItemRequest) = viewModelScope.launch {
        val JSESSIONID = AirbankApplication.prefs.getString("JSESSIONID","")
        Log.d("ViewModel", "Stored JSESSIONID: $JSESSIONID")
        _createItemState.emit(Resource(State.LOADING, null, null))
        try {
            val response = savingsRepository.createSavingsItem(request)
            Log.d("CreateItem","티클 모으기 성공: ${response.data}")
            _createItemState.emit(response)

        } catch (e: Exception) {
            Log.d("티끌 에러 타입", e.message.toString())
            Log.d("CreateItem","티클 모으기 실패")
            _createItemState.emit(Resource(State.ERROR, null, e.localizedMessage))
        }
    }

    private val _updateSavingsState = MutableStateFlow<Resource<UpdateSavingsResponse>>(Resource(State.LOADING, null, null))
    val updateSavingsState: StateFlow<Resource<UpdateSavingsResponse>> get() = _updateSavingsState


    fun updateSavings(groupId: Int, request: UpdateSavingsRequest) = viewModelScope.launch {
        _updateSavingsState.emit(Resource(State.LOADING, null, null))
        try {
            val response = savingsRepository.updateSavings(groupId, request)
            _updateSavingsState.emit(response)
        } catch (e: Exception) {
            _updateSavingsState.emit(Resource(State.ERROR, null, e.localizedMessage))
        }
    }

    private val _cancelSavingsState = MutableStateFlow<Resource<CancelSavingsResponse>>(Resource(State.LOADING, null, null))
    val cancelSavingsState: StateFlow<Resource<CancelSavingsResponse>> get() = _cancelSavingsState

    fun cancelSavings(request: CancelSavingsRequest) = viewModelScope.launch {
        _cancelSavingsState.emit(Resource(State.LOADING, null, null))
        try {
            val response = savingsRepository.cancelSavings(request)
            _cancelSavingsState.emit(response)
            Log.d("CancelItem","티끌 모으기 실패: ${response.data?.message}")
            Log.d("CancelItem","티끌 모으기 실패: ${response.data?.code}")
        } catch (e: Exception) {
            _cancelSavingsState.emit(Resource(State.ERROR, null, e.localizedMessage))
        }
    }

    private val _remitSavingsState = MutableStateFlow<Resource<SavingsRemitResponse>>(Resource(State.LOADING, null, null))
    val remitSavingsState: StateFlow<Resource<SavingsRemitResponse>> get() = _remitSavingsState


    fun remitSavings(request: SavingsRemitRequest) = viewModelScope.launch {
        _remitSavingsState.emit(Resource(State.LOADING, null, null))
        try {
            val response = savingsRepository.remitSavings(request)
            _remitSavingsState.emit(response)

        } catch (e: Exception) {
            _remitSavingsState.emit(Resource(State.ERROR, null, e.localizedMessage))
        }
    }


    private val _bonusSavingState = MutableStateFlow<Resource<BonusSavingsResponse>>(Resource(State.LOADING, null, null))
    val bonusSavingsState: StateFlow<Resource<BonusSavingsResponse>> get() = _bonusSavingState

    fun bonusSavings(groupId: Int, request: BonusSavingsRequest) = viewModelScope.launch {
        _bonusSavingState.emit(Resource(State.LOADING, null, null))
        try {
            val response = savingsRepository.bonusSavings(groupId, request)
            _bonusSavingState.emit(response)
        } catch (e: Exception) {
            _bonusSavingState.emit(Resource(State.ERROR, null, e.localizedMessage))
        }
    }

    private val _getNotificationsState = MutableStateFlow<Resource<NotificationResponse>>(Resource(State.LOADING,null,null))
    val getNotificationsState: StateFlow<Resource<NotificationResponse>> get() = _getNotificationsState

    fun getNotifications(groupId: Int) = viewModelScope.launch{
        _getNotificationsState.emit(Resource(State.LOADING, null, null))
        try{
            val response = savingsRepository.getNotifications(groupId)
            _getNotificationsState.emit(response)
            Log.d("알림뷰","알림메: ${response.data?.message}")
            Log.d("알림뷰","알림코: ${response.data?.code}")
        } catch(e: Exception) {
            _getNotificationsState.emit(Resource(State.ERROR, null, e.localizedMessage))
        }

    }
}
