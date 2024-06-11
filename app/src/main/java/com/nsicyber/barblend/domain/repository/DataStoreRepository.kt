package com.nsicyber.barblend.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun compareCocktails(newData: String): Flow<Boolean?>
}
