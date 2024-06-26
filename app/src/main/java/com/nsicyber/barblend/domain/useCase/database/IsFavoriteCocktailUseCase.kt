package com.nsicyber.barblend.domain.useCase.database

import com.nsicyber.barblend.common.DaoResult
import com.nsicyber.barblend.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsFavoriteCocktailUseCase
    @Inject
    constructor(
        private val repo: DataRepository,
    ) {
        operator fun invoke(id: String?): Flow<DaoResult<Boolean>> =
            flow {
                try {
                    if (repo.isFavoriteCocktail(id) != 0) {
                        emit(DaoResult.Success(true))
                    } else {
                        emit(DaoResult.Success(false))
                    }
                } catch (e: Exception) {
                    emit(DaoResult.Error(message = e.message.toString()))
                }
            }
    }
