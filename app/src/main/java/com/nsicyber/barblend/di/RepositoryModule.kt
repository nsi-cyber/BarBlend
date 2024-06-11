package com.nsicyber.barblend.di

import android.content.Context
import com.nsicyber.barblend.data.datastore.DataStoreManager
import com.nsicyber.barblend.data.local.dao.CocktailsDao
import com.nsicyber.barblend.data.remote.ApiService
import com.nsicyber.barblend.data.repository.DataRepositoryImpl
import com.nsicyber.barblend.data.repository.DataStoreRepositoryImpl
import com.nsicyber.barblend.domain.repository.DataRepository
import com.nsicyber.barblend.domain.repository.DataStoreRepository
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
    fun provideDataStoreManager(
        @ApplicationContext context: Context,
    ): DataStoreManager = DataStoreManager(context)

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStoreManager: DataStoreManager): DataStoreRepository = DataStoreRepositoryImpl(dataStoreManager)

    @Provides
    @Singleton
    fun provideDataRepository(
        api: ApiService,
        database: CocktailsDao,
    ): DataRepository = DataRepositoryImpl(api, database)
}
