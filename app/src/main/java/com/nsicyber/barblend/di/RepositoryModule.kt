package com.nsicyber.barblend.di

import android.content.Context
import androidx.room.Room
import com.nsicyber.barblend.data.local.dao.CocktailsDao
import com.nsicyber.barblend.data.local.database.CocktailDatabase
import com.nsicyber.barblend.data.repository.DataRepositoryImpl
import com.nsicyber.barblend.data.repository.DatabaseRepositoryImpl
import com.nsicyber.barblend.domain.repository.ApiRepository
import com.nsicyber.barblend.domain.repository.DataRepository
import com.nsicyber.barblend.domain.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun provideDataRepository(
        api: ApiRepository,
        database: DatabaseRepository
    ): DataRepository {
        return DataRepositoryImpl(api, database)
    }
}