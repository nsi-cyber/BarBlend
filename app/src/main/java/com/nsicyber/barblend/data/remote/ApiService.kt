package com.nsicyber.barblend.data.remote

import com.nsicyber.barblend.data.remote.model.CocktailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("/popular.php")
    suspend fun getPopularCocktails(): Response<CocktailResponse?>?

    @GET("/lookup.php")
    suspend fun getCocktailById(@Query("i") cocktailId: String?): Response<CocktailResponse?>?
    @GET("/search.php")
    suspend fun searchCocktailByName(@Query("s") cocktailName: String?): Response<CocktailResponse?>?

    @GET("/random.php")
    suspend fun getRandomCocktail(): Response<CocktailResponse?>?

    @GET("/latest.php")
    suspend fun getLatestCocktails(): Response<CocktailResponse?>?


}