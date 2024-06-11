package com.nsicyber.barblend.data.repository

import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.common.DaoResult
import com.nsicyber.barblend.data.local.dao.CocktailsDao
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.data.remote.ApiService
import com.nsicyber.barblend.data.toFavLocal
import com.nsicyber.barblend.data.toLocal
import com.nsicyber.barblend.data.toModel
import com.nsicyber.barblend.data.util.apiFlow
import com.nsicyber.barblend.data.util.daoFlow
import com.nsicyber.barblend.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataRepositoryImpl
    @Inject
    constructor(
        private val api: ApiService,
        private val database: CocktailsDao,
    ) : DataRepository {
        override fun getCocktailDetail(id: String?): Flow<ApiResult<CocktailModel?>> =
            apiFlow { api.getCocktailById(id) }.map { result ->
                mapApiResult(result) { it?.drinks?.firstOrNull()?.toModel() }
            }

        override fun getRandomCocktailDetail(): Flow<ApiResult<CocktailModel?>> =
            apiFlow { api.getRandomCocktail() }
                .map { result -> mapApiResult(result) { it?.drinks?.firstOrNull()?.toModel() } }

        override fun getPopularCocktailList(): Flow<ApiResult<List<CocktailModel?>?>> =
            apiFlow { api.getPopularCocktails() }
                .map { result -> mapApiResult(result) { it?.drinks?.map { drink -> drink?.toModel() } } }

        override fun getLatestCocktailList(): Flow<ApiResult<List<CocktailModel?>?>> =
            apiFlow { api.getLatestCocktails() }
                .map { result -> mapApiResult(result) { it?.drinks?.map { drink -> drink?.toModel() } } }

        override fun getFavoriteCocktailDetailFromDao(id: String?): Flow<DaoResult<CocktailModel?>> =
            daoFlow { database.getFavoriteCocktailDetailFromDao(id) }
                .map { result -> mapDaoResult(result) { it.toModel() } }

        override suspend fun isFavoriteCocktail(id: String?): Int {
            return database.isFavoriteCocktail(id)
        }

        override fun getFavoriteCocktailsFromDao(): Flow<DaoResult<List<CocktailModel?>?>> =
            daoFlow { database.getFavoriteCocktailsFromDao() }
                .map { result -> mapDaoResult(result) { it.map { it.toModel() } } }

        override fun getRecentCocktailsFromDao(): Flow<DaoResult<List<CocktailModel?>?>> =
            daoFlow { database.getCocktailsFromDao() }
                .map { result ->
                    mapDaoResult(result) {
                        it.map { it.toModel() }
                    }
                }

        override suspend fun saveToFavoriteDao(cocktail: CocktailModel) {
            daoFlow { database.saveCocktailToFavorite(cocktail.toFavLocal()) }.collect()
        }

        override suspend fun saveToRecentDao(cocktail: CocktailModel) {
            daoFlow { database.saveCocktailToRecent(cocktail.toLocal()) }.collect()
        }

        override suspend fun removeFromDao(cocktail: CocktailModel) {
            daoFlow { database.removeCocktailFromFavorites(cocktail.toFavLocal()) }.collect()
        }

        override fun searchCocktailByName(name: String): Flow<ApiResult<List<CocktailModel?>?>> =
            apiFlow { api.searchCocktailByName(name) }
                .map { result -> mapApiResult(result) { it?.drinks?.map { drink -> drink?.toModel() } } }
    }

private fun <T, R> mapDaoResult(
    result: DaoResult<T>,
    transform: (T) -> R?,
): DaoResult<R?> {
    return when (result) {
        is DaoResult.Success -> {
            val transformedData = result.data?.let { transform(it) }
            if (transformedData != null) {
                DaoResult.Success(transformedData)
            } else {
                DaoResult.Error("No data found")
            }
        }

        is DaoResult.Error -> DaoResult.Error(result.message)
    }
}

private fun <T, R> mapApiResult(
    result: ApiResult<T>,
    transform: (T) -> R?,
): ApiResult<R?> {
    return when (result) {
        is ApiResult.Success -> {
            val transformedData = result.data?.let { transform(it) }
            if (transformedData != null) {
                ApiResult.Success(transformedData)
            } else {
                ApiResult.Error("No data found")
            }
        }

        is ApiResult.Error -> ApiResult.Error(result.message)
    }
}
