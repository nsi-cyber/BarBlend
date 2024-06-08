package com.nsicyber.barblend.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.nsicyber.barblend.domain.repository.DataStoreRepository
import com.nsicyber.barblend.domain.repository.NotificationRepository
import com.nsicyber.barblend.domain.useCase.datastore.ComparePreferencesUseCase
import com.nsicyber.barblend.domain.useCase.network.GetLatestCocktailsUseCase
import javax.inject.Inject

class NotificationWorkerFactory @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val getLatestCocktailsUseCase: GetLatestCocktailsUseCase,
    private val comparePreferencesUseCase: ComparePreferencesUseCase
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return NotificationWorker(appContext, workerParameters, notificationRepository,getLatestCocktailsUseCase,comparePreferencesUseCase)
    }
}