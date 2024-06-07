package com.nsicyber.barblend.data.local.database



import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nsicyber.barblend.data.local.converters.Converters
import com.nsicyber.barblend.data.local.dao.CocktailsDao
import com.nsicyber.barblend.data.local.entity.CocktailFavoriteLocal
import com.nsicyber.barblend.data.local.entity.CocktailLocal

@Database(entities = [CocktailLocal::class,CocktailFavoriteLocal::class], version = 1)
@TypeConverters(Converters::class)
abstract class CocktailDatabase : RoomDatabase() {
    abstract fun cocktailsDao(): CocktailsDao

}

