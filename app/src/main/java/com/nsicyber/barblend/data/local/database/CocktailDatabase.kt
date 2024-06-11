package com.nsicyber.barblend.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nsicyber.barblend.data.local.converters.Converters
import com.nsicyber.barblend.data.local.dao.CocktailsDao
import com.nsicyber.barblend.data.local.entity.CocktailEntity
import com.nsicyber.barblend.data.local.entity.CocktailFavoriteEntity

@Database(entities = [CocktailEntity::class, CocktailFavoriteEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CocktailDatabase : RoomDatabase() {
    abstract fun cocktailsDao(): CocktailsDao
}
