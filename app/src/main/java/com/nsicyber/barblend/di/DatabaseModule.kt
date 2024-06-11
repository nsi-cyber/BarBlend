package com.nsicyber.barblend.di

import android.content.Context
import androidx.room.Room
import com.nsicyber.barblend.common.Constants
import com.nsicyber.barblend.data.local.dao.CocktailsDao
import com.nsicyber.barblend.data.local.database.CocktailDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideCocktailDatabase(
        @ApplicationContext appContext: Context,
    ): CocktailDatabase =
        Room.databaseBuilder(
            appContext,
            CocktailDatabase::class.java,
            Constants.COCKTAIL_DATABASE_NAME,
        ).build()

    @Provides
    fun provideCocktailDao(cocktailDatabase: CocktailDatabase): CocktailsDao = cocktailDatabase.cocktailsDao()
}
