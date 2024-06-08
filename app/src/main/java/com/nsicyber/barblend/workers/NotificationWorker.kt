package com.nsicyber.barblend.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.domain.repository.NotificationRepository
import com.nsicyber.barblend.domain.useCase.datastore.ComparePreferencesUseCase
import com.nsicyber.barblend.domain.useCase.network.GetLatestCocktailsUseCase
import javax.inject.Inject

@HiltWorker
class NotificationWorker @Inject constructor(
    context: Context,
    workerParams: WorkerParameters,
    private val notificationRepository: NotificationRepository,
    private val getLatestCocktailsUseCase: GetLatestCocktailsUseCase,
    private val comparePreferencesUseCase: ComparePreferencesUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Log.e("doWork: ", "NotificationWorker Successful")
            getLatestCocktailsUseCase.invoke().collect { result ->
                when (result) {
                    is ApiResult.Error -> {}
                    ApiResult.Loading -> {}
                    is ApiResult.Success -> {
                        comparePreferencesUseCase(result.data?.drinks.toString())
                            .collect { resultCompare ->
                                when (resultCompare) {
                                    true -> notificationRepository.pushNotification()
                                    false -> {}
                                    null -> {}
                                }
                            }
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}