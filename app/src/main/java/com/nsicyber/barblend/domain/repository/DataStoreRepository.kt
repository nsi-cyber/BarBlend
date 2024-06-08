package com.nsicyber.barblend.domain.repository


import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun compare(newData:String): Flow<Boolean?>
}