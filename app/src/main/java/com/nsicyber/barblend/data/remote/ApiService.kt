package com.nsicyber.barblend.data.remote

import com.nsicyber.barblend.common.Constants
import com.nsicyber.barblend.data.remote.model.CocktailListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(Constants.Endpoints.POPULAR)
    suspend fun getPopularCocktails(): Response<CocktailListResponse?>?

    @GET(Constants.Endpoints.LOOKUP)
    suspend fun getCocktailById(
        @Query("i") cocktailId: String?,
    ): Response<CocktailListResponse?>?

    @GET(Constants.Endpoints.SEARCH)
    suspend fun searchCocktailByName(
        @Query("s") cocktailName: String?,
    ): Response<CocktailListResponse?>?

    @GET(Constants.Endpoints.RANDOM)
    suspend fun getRandomCocktail(): Response<CocktailListResponse?>?

    @GET(Constants.Endpoints.LATEST)
    suspend fun getLatestCocktails(): Response<CocktailListResponse?>?
}
