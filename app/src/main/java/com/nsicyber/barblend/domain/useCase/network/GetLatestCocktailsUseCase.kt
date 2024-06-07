package com.nsicyber.barblend.domain.useCase.network

import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.remote.model.CocktailResponse
import com.nsicyber.barblend.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetLatestCocktailsUseCase @Inject constructor(
    private val repo: DataRepository
) {
    operator fun invoke(): Flow<ApiResult<CocktailResponse?>> =
        flow {
            try {
                emit(ApiResult.Loading)
                repo.getLatestCocktailList().collect { result ->
                    emit(result)
                }
            } catch (e: Exception) {

                emit(ApiResult.Error(message = e.message.toString()))

            }
        }
}