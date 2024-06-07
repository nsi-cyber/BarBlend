package com.nsicyber.barblend.domain.repository

import com.nsicyber.barblend.data.local.entity.CocktailFavoriteLocal
import com.nsicyber.barblend.data.local.entity.CocktailLocal
import kotlinx.coroutines.flow.Flow


interface DatabaseRepository {



    suspend fun getFavoriteCocktailDetailFromDao(id:String?): Flow<CocktailFavoriteLocal>
    suspend fun getFavoriteCocktailsFromDao(): Flow<List<CocktailFavoriteLocal>>
    suspend fun getRecentCocktailsFromDao():  Flow<List<CocktailLocal>>
    suspend fun saveToFavoriteDao(cocktail:CocktailFavoriteLocal)
    suspend fun saveToRecentDao(cocktail:CocktailLocal)
    suspend fun isFavoriteCocktail(cocktailId:String?):Int
    suspend fun removeFromDao(cocktail:CocktailFavoriteLocal)



}