package com.nsicyber.barblend.data.repository


import com.nsicyber.barblend.data.datastore.DataStoreManager
import com.nsicyber.barblend.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(private val dataStoreManager: DataStoreManager) :
    DataStoreRepository {
    override suspend fun compare(newData: String): Flow<Boolean?> {
      return  dataStoreManager.compare(newData)
    }


}