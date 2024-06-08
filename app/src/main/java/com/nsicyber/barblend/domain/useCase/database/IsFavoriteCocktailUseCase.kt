package com.nsicyber.barblend.domain.useCase.database

import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class IsFavoriteCocktailUseCase @Inject constructor(
    private val repo: DataRepository
) {
    operator fun invoke(id:String?): Flow<ApiResult<Boolean>> =
        flow {
            try {
                emit(ApiResult.Loading)
                if(repo.isFavoriteCocktail(id)!=0 )
                    emit(ApiResult.Success(true))
                else
                    emit(ApiResult.Success(false))


            } catch (e: Exception) {

                emit(ApiResult.Error(message = e.message.toString()))

            }
        }
}