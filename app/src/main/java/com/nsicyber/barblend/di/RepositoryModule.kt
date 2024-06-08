package com.nsicyber.barblend.di

import android.content.Context
import com.nsicyber.barblend.data.datastore.DataStoreManager
import com.nsicyber.barblend.data.repository.DataRepositoryImpl
import com.nsicyber.barblend.data.repository.DataStoreRepositoryImpl
import com.nsicyber.barblend.data.repository.NotificationRepositoryImpl
import com.nsicyber.barblend.domain.repository.ApiRepository
import com.nsicyber.barblend.domain.repository.DataRepository
import com.nsicyber.barblend.domain.repository.DataStoreRepository
import com.nsicyber.barblend.domain.repository.DatabaseRepository
import com.nsicyber.barblend.domain.repository.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStoreManager: DataStoreManager): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStoreManager)
    }



    @Provides
    @Singleton
    fun provideDataRepository(
        api: ApiRepository,
        database: DatabaseRepository
    ): DataRepository {
        return DataRepositoryImpl(api, database)
    }


    @Provides
    @Singleton
    fun provideNotificationRepository(
        @ApplicationContext context: Context
    ): NotificationRepository {
        return NotificationRepositoryImpl(context)
    }
}