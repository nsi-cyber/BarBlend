package com.nsicyber.barblend.data.repository


import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.remote.ApiService
import com.nsicyber.barblend.data.remote.model.CocktailResponse
import com.nsicyber.barblend.data.util.apiFlow
import com.nsicyber.barblend.domain.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ApiRepository {


    override suspend fun getCocktailDetail(id: String?): Flow<ApiResult<CocktailResponse?>> =
        apiFlow {
            api.getCocktailById(id)
        }

    override suspend fun getRandomCocktailDetail(): Flow<ApiResult<CocktailResponse?>> = apiFlow {
        api.getRandomCocktail()
    }

    override suspend fun getPopularCocktailList(): Flow<ApiResult<CocktailResponse?>> = apiFlow {
        api.getPopularCocktails()
    }

    override suspend fun getLatestCocktailList(): Flow<ApiResult<CocktailResponse?>> = apiFlow {
        api.getLatestCocktails()
    }

    override suspend fun searchCocktailByName(name: String): Flow<ApiResult<CocktailResponse?>> =
        apiFlow {
            api.searchCocktailByName(name)
        }
}