package com.nsicyber.barblend.data.util

import android.util.Log
import com.nsicyber.barblend.common.DaoResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

fun <T> daoFlow(call: suspend () -> T): Flow<DaoResult<T>> =
    flow {
        try {
            val result = call()
            emit(DaoResult.Success(result))
        } catch (e: Exception) {
            Log.e("DaoFlow", e.message ?: "An error occurred")
            emit(DaoResult.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)
