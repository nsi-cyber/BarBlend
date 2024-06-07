package com.nsicyber.barblend.domain.useCase.database

import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.local.entity.CocktailFavoriteLocal
import com.nsicyber.barblend.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RemoveCocktailUseCase @Inject constructor(
    private val repo: DataRepository
) {
    operator fun invoke(cocktail: CocktailFavoriteLocal): Flow<ApiResult<String>> =
        flow {
            try {
                emit(ApiResult.Loading)
                repo.removeFromDao(cocktail)
                emit(ApiResult.Success(null))

            } catch (e: Exception) {

                emit(ApiResult.Error(message = e.message.toString()))

            }
        }
}