package com.nsicyber.barblend

import android.app.Application
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.nsicyber.barblend.workers.NotificationWorker
import com.nsicyber.barblend.workers.NotificationWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class BarBlendApplication : Application(), Configuration.Provider {


    @Inject
    lateinit var workerFactory: NotificationWorkerFactory

    @Inject
    lateinit var workManager: WorkManager


    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        workManager.enqueueUniquePeriodicWork(
            "NotificationWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            PeriodicWorkRequestBuilder<NotificationWorker>(
                15, TimeUnit.SECONDS
            )
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )
                .build()
        )
    }


}