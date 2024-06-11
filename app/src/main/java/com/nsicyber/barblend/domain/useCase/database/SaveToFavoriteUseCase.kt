package com.nsicyber.barblend.domain.useCase.database

import com.nsicyber.barblend.common.DaoResult
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveToFavoriteUseCase
    @Inject
    constructor(
        private val repo: DataRepository,
    ) {
        operator fun invoke(cocktail: CocktailModel): Flow<DaoResult<String>> =
            flow {
                try {
                    repo.saveToFavoriteDao(cocktail)
                    emit(DaoResult.Success(null))
                } catch (e: Exception) {
                    emit(DaoResult.Error(message = e.message.toString()))
                }
            }
    }
