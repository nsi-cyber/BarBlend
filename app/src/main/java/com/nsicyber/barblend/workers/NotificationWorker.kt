package com.nsicyber.barblend.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.domain.useCase.datastore.ComparePreferencesUseCase
import com.nsicyber.barblend.domain.useCase.network.GetLatestCocktailsUseCase
import com.nsicyber.barblend.utils.NotificationUtils
import javax.inject.Inject

@HiltWorker
class NotificationWorker
    @Inject
    constructor(
        context: Context,
        workerParams: WorkerParameters,
        private val getLatestCocktailsUseCase: GetLatestCocktailsUseCase,
        private val comparePreferencesUseCase: ComparePreferencesUseCase,
    ) : CoroutineWorker(context, workerParams) {
        override suspend fun doWork(): Result {
            return try {
                Log.d("NotificationWorker: doWork/ ", "Started ")
                getLatestCocktailsUseCase.invoke().collect { result ->
                    when (result) {
                        is ApiResult.Error -> {}
                        is ApiResult.Success -> {
                            Log.d("NotificationWorker: doWork/ ", "ApiResult.Success ")

                            comparePreferencesUseCase(result.data.toString())
                                .collect { resultCompare ->
                                    when (resultCompare) {
                                        true -> NotificationUtils.pushNotification(this.applicationContext)
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
