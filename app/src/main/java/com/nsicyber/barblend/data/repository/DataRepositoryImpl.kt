package com.nsicyber.barblend.data.repository

import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.local.entity.CocktailFavoriteLocal
import com.nsicyber.barblend.data.local.entity.CocktailLocal
import com.nsicyber.barblend.data.remote.model.CocktailResponse
import com.nsicyber.barblend.data.toLocal
import com.nsicyber.barblend.domain.repository.ApiRepository
import com.nsicyber.barblend.domain.repository.DataRepository
import com.nsicyber.barblend.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DataRepositoryImpl @Inject constructor(
    private val api: ApiRepository,
    private val database: DatabaseRepository
) : DataRepository {
    override suspend fun getCocktailDetail(id: String?): Flow<ApiResult<CocktailResponse?>> {
        val response = api.getCocktailDetail(id)
        response.collect { value ->
            when (value) {
                is ApiResult.Success -> {
                    value.data?.drinks?.first()?.let {
                        saveToRecentDao(it.toLocal())
                    }
                }

                else -> {
                    // ignored
                }
            }
        }
        return response
    }

    override suspend fun getRandomCocktailDetail(): Flow<ApiResult<CocktailResponse?>> {
        return api.getRandomCocktailDetail()
    }

    override suspend fun getPopularCocktailList(): Flow<ApiResult<CocktailResponse?>> {
        return api.getPopularCocktailList()
    }

    override suspend fun getLatestCocktailList(): Flow<ApiResult<CocktailResponse?>> {
        return api.getLatestCocktailList()
    }



    override suspend fun getFavoriteCocktailDetailFromDao(id: String?): Flow<CocktailFavoriteLocal> {
        return database.getFavoriteCocktailDetailFromDao(id)
    }

    override suspend fun isFavoriteCocktail(id: String?): Int {
        return database.isFavoriteCocktail(id)
    }

    override suspend fun getFavoriteCocktailsFromDao(): Flow<List<CocktailFavoriteLocal>> {
        return database.getFavoriteCocktailsFromDao()
    }

    override suspend fun getRecentCocktailsFromDao(): Flow<List<CocktailLocal>> {
        return database.getRecentCocktailsFromDao()
    }

    override suspend fun saveToFavoriteDao(cocktail: CocktailFavoriteLocal){
        return database.saveToFavoriteDao(cocktail)
    }

    override suspend fun saveToRecentDao(cocktail: CocktailLocal) {
        return database.saveToRecentDao(cocktail)
    }

    override suspend fun removeFromDao(cocktail: CocktailFavoriteLocal) {
        return database.removeFromDao(cocktail)
    }

    override suspend fun searchCocktailByName(name: String): Flow<ApiResult<CocktailResponse?>> {
        return api.searchCocktailByName(name)
    }


}