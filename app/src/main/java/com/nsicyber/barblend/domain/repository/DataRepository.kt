package com.nsicyber.barblend.domain.repository

import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.local.entity.CocktailFavoriteLocal
import com.nsicyber.barblend.data.local.entity.CocktailLocal
import com.nsicyber.barblend.data.remote.model.CocktailResponse
import kotlinx.coroutines.flow.Flow


interface DataRepository {

    suspend fun getCocktailDetail(id:String?): Flow<ApiResult<CocktailResponse?>>
    suspend fun getRandomCocktailDetail(): Flow<ApiResult<CocktailResponse?>>
    suspend fun getPopularCocktailList(): Flow<ApiResult<CocktailResponse?>>
    suspend fun getLatestCocktailList(): Flow<ApiResult<CocktailResponse?>>
    suspend fun searchCocktailByName(name:String): Flow<ApiResult<CocktailResponse?>>


    suspend fun getFavoriteCocktailDetailFromDao(id:String?): Flow<CocktailFavoriteLocal>
    suspend fun isFavoriteCocktail(id:String?): Int
    suspend fun getFavoriteCocktailsFromDao(): Flow<List<CocktailFavoriteLocal>>
    suspend fun getRecentCocktailsFromDao():  Flow<List<CocktailLocal>>
    suspend fun saveToFavoriteDao(cocktail: CocktailFavoriteLocal)
    suspend fun saveToRecentDao(cocktail: CocktailLocal)
    suspend fun removeFromDao(cocktail: CocktailFavoriteLocal)



}