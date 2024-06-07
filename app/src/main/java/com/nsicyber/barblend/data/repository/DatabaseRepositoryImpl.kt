package com.nsicyber.barblend.data.repository

import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.local.dao.CocktailsDao
import com.nsicyber.barblend.data.local.entity.CocktailFavoriteLocal
import com.nsicyber.barblend.data.local.entity.CocktailLocal
import com.nsicyber.barblend.data.remote.ApiService
import com.nsicyber.barblend.data.remote.model.CocktailResponse
import com.nsicyber.barblend.data.util.apiFlow
import com.nsicyber.barblend.domain.repository.ApiRepository
import com.nsicyber.barblend.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DatabaseRepositoryImpl @Inject constructor(
    private val database: CocktailsDao

) : DatabaseRepository {

    override suspend fun getFavoriteCocktailsFromDao(): Flow<List<CocktailFavoriteLocal>> {
        return database.getFavoriteCocktailsFromDao()
    }
    override suspend fun getFavoriteCocktailDetailFromDao(id: String?): Flow<CocktailFavoriteLocal> {
        return database.getFavoriteCocktailDetailFromDao(id)
    }

    override suspend fun getRecentCocktailsFromDao(): Flow<List<CocktailLocal>> {
        return database.getCocktailsFromDao()
    }

    override suspend fun saveToFavoriteDao(cocktail: CocktailFavoriteLocal) {
      return   database.saveCocktailToFavorite(cocktail)
    }

    override suspend fun saveToRecentDao(cocktail: CocktailLocal){
      return  database.saveCocktailToRecent(cocktail)
    }

    override suspend fun isFavoriteCocktail(cocktailId: String?): Int {
        return database.isFavoriteCocktail(cocktailId)
    }

    override suspend fun removeFromDao(cocktail: CocktailFavoriteLocal){

       return  database.removeCocktailFromFavorites(cocktail)
    }


}