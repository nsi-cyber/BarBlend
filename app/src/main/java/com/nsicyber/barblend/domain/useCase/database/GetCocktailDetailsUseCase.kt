package com.nsicyber.barblend.domain.useCase.database

import com.nsicyber.barblend.common.DaoResult
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCocktailDetailsUseCase
    @Inject
    constructor(
        private val repo: DataRepository,
    ) {
        operator fun invoke(id: String?): Flow<DaoResult<CocktailModel?>> =
            flow {
                try {
                    repo.getFavoriteCocktailDetailFromDao(id).collect { result ->
                        emit(result)
                    }
                } catch (e: Exception) {
                    emit(DaoResult.Error(message = e.message.toString()))
                }
            }
    }
