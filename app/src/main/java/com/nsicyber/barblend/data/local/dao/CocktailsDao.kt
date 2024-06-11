package com.nsicyber.barblend.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nsicyber.barblend.data.local.entity.CocktailEntity
import com.nsicyber.barblend.data.local.entity.CocktailFavoriteEntity

@Dao
interface CocktailsDao {
    @Query("SELECT * FROM cocktail")
    suspend fun getCocktailsFromDao(): List<CocktailEntity>

    @Query("SELECT * FROM cocktail_favorite")
    suspend fun getFavoriteCocktailsFromDao(): List<CocktailFavoriteEntity>

    @Query("SELECT COUNT(*) FROM cocktail_favorite WHERE id = :cocktailId")
    suspend fun isFavoriteCocktail(cocktailId: String?): Int

    @Query("SELECT * FROM cocktail_favorite WHERE id=:cocktailId ")
    suspend fun getFavoriteCocktailDetailFromDao(cocktailId: String?): CocktailFavoriteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCocktailToRecent(cocktail: CocktailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCocktailToFavorite(cocktail: CocktailFavoriteEntity)

    @Delete
    suspend fun removeCocktailFromFavorites(conversation: CocktailFavoriteEntity)
}
