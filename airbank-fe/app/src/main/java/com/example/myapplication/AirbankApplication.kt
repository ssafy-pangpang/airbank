package com.example.myapplication

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myapplication.Util.PreferenceUtil
import com.example.myapplication.workers.NotificationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
@HiltAndroidApp
class AirbankApplication : Application() {

    companion object {
        lateinit var prefs: PreferenceUtil
            private set
    }

    override fun onCreate() {
        super.onCreate()

        prefs = PreferenceUtil(applicationContext)

        setupNotificationChecker()
    }

    private fun setupNotificationChecker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationCheckRequest = PeriodicWorkRequestBuilder<NotificationWorker>(30, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "NotificationCheck",
            ExistingPeriodicWorkPolicy.REPLACE,
            notificationCheckRequest
        )
    }
}

