package com.nsicyber.barblend.data.local.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nsicyber.barblend.data.local.entity.CocktailFavoriteLocal
import com.nsicyber.barblend.data.local.entity.CocktailLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailsDao {
    //GetCache
    @Query("SELECT * FROM cocktail WHERE id=:cocktailId ")
     fun getCocktailDetailFromDao(cocktailId: String?): Flow<CocktailLocal>

    //GetRecents
    @Query("SELECT * FROM cocktail")
     fun getCocktailsFromDao(): Flow<List<CocktailLocal>>
    //GetFavorites
    @Query("SELECT * FROM cocktail_favorite")
     fun getFavoriteCocktailsFromDao(): Flow<List<CocktailFavoriteLocal>>

    @Query("SELECT COUNT(*) FROM cocktail_favorite WHERE id = :cocktailId")
    suspend fun isFavoriteCocktail(cocktailId: String?): Int

    //GetCache
    @Query("SELECT * FROM cocktail_favorite WHERE id=:cocktailId ")
     fun getFavoriteCocktailDetailFromDao(cocktailId: String?): Flow<CocktailFavoriteLocal>

    //saveRecent
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun saveCocktailToRecent(cocktail: CocktailLocal)

    //saveFavorite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCocktailToFavorite(cocktail: CocktailFavoriteLocal)

    //removeFavorite
    @Delete
    suspend   fun removeCocktailFromFavorites(conversation: CocktailFavoriteLocal)

    //updateCocktail
    @Update
    suspend fun updateCocktail(cocktail: CocktailLocal)

    //updateFavoriteCocktail
    @Update
    suspend  fun updateFavoriteCocktail(cocktail: CocktailFavoriteLocal)

}