package com.nsicyber.barblend.di

import android.app.Application
import androidx.work.WorkManager
import com.nsicyber.barblend.domain.useCase.datastore.ComparePreferencesUseCase
import com.nsicyber.barblend.domain.useCase.network.GetLatestCocktailsUseCase
import com.nsicyber.barblend.workers.NotificationWorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
    @Singleton
    @Provides
    fun provideNotificationWorkerFactory(
        getLatestCocktailsUseCase: GetLatestCocktailsUseCase,
        comparePreferencesUseCase: ComparePreferencesUseCase,
    ) = NotificationWorkerFactory(getLatestCocktailsUseCase, comparePreferencesUseCase)

    @Singleton
    @Provides
    fun provideWorkManager(app: Application) = WorkManager.getInstance(app.applicationContext)
}
