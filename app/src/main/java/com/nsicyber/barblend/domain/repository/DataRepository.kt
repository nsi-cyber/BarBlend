package com.nsicyber.barblend.domain.repository

import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.common.DaoResult
import com.nsicyber.barblend.data.model.CocktailModel
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getCocktailDetail(id: String?): Flow<ApiResult<CocktailModel?>>

    fun getRandomCocktailDetail(): Flow<ApiResult<CocktailModel?>>

    fun getPopularCocktailList(): Flow<ApiResult<List<CocktailModel?>?>>

    fun getLatestCocktailList(): Flow<ApiResult<List<CocktailModel?>?>>

    fun searchCocktailByName(name: String): Flow<ApiResult<List<CocktailModel?>?>>

    fun getFavoriteCocktailDetailFromDao(id: String?): Flow<DaoResult<CocktailModel?>>

    suspend fun isFavoriteCocktail(id: String?): Int

    fun getFavoriteCocktailsFromDao(): Flow<DaoResult<List<CocktailModel?>?>>

    fun getRecentCocktailsFromDao(): Flow<DaoResult<List<CocktailModel?>?>>

    suspend fun saveToFavoriteDao(cocktail: CocktailModel)

    suspend fun saveToRecentDao(cocktail: CocktailModel)

    suspend fun removeFromDao(cocktail: CocktailModel)
}
