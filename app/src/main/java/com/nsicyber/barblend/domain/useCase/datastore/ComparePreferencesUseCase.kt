package com.nsicyber.barblend.domain.useCase.datastore

import com.nsicyber.barblend.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ComparePreferencesUseCase
    @Inject
    constructor(private val repository: DataStoreRepository) {
        suspend operator fun invoke(newData: String): Flow<Boolean?> = repository.compareCocktails(newData)
    }
