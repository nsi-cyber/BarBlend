package com.nsicyber.barblend.domain.useCase.network

import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchCocktailByNameUseCase
    @Inject
    constructor(
        private val repo: DataRepository,
    ) {
        operator fun invoke(name: String): Flow<
            ApiResult<List<CocktailModel?>?>,
        > =
            flow {
                try {
                    repo.searchCocktailByName(name).collect { result ->
                        emit(result)
                    }
                } catch (e: Exception) {
                    emit(ApiResult.Error(message = e.message.toString()))
                }
            }
    }
