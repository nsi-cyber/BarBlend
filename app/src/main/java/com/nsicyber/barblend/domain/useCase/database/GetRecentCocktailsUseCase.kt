package com.nsicyber.barblend.domain.useCase.database

import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.local.entity.CocktailLocal
import com.nsicyber.barblend.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetRecentCocktailsUseCase @Inject constructor(
    private val repo: DataRepository
) {
    operator fun invoke(): Flow<ApiResult<List<CocktailLocal>>> =
        flow {
            try {
                emit(ApiResult.Loading)
                repo.getRecentCocktailsFromDao().collect { result ->
                    emit(ApiResult.Success(result))
                }
            } catch (e: Exception) {

                emit(ApiResult.Error(message = e.message.toString()))

            }
        }
}