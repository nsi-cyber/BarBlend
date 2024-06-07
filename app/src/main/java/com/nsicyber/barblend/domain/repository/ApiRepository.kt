package com.nsicyber.barblend.domain.repository

import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.remote.model.CocktailResponse
import kotlinx.coroutines.flow.Flow

interface ApiRepository {

    suspend fun getCocktailDetail(id:String?): Flow<ApiResult<CocktailResponse?>>
    suspend fun getRandomCocktailDetail(): Flow<ApiResult<CocktailResponse?>>
    suspend fun getPopularCocktailList(): Flow<ApiResult<CocktailResponse?>>
    suspend fun getLatestCocktailList(): Flow<ApiResult<CocktailResponse?>>
    suspend fun searchCocktailByName(name:String): Flow<ApiResult<CocktailResponse?>>




}