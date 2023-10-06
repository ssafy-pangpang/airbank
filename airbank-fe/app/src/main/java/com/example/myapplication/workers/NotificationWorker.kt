package com.example.myapplication.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.AirbankApplication
import com.example.myapplication.api.ApiService
import com.example.myapplication.repository.SavingsRepository
import dagger.assisted.AssistedInject

class NotificationWorker @AssistedInject constructor(
    appContext: Context,
    workerParams: WorkerParameters,
    private val savingsRepository: SavingsRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        var updateAlarm = false
    }
    val groupId = AirbankApplication.prefs.getString("group_id", "")
    override suspend fun doWork(): Result {
        val response = savingsRepository.getNotifications(groupId.toInt())
        val notifications = response.data?.data?.notificationElements
        if (notifications?.any { it.activated } == true) {
            updateAlarm = true
            Log.d("NotificationWorker", "알림신청했습니다.")
        } else {
            updateAlarm = false
        }
        return Result.success()
    }
}
