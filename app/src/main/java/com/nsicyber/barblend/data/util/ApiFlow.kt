package com.nsicyber.barblend.data.util


import android.util.Log
import com.google.gson.Gson
import com.nsicyber.barblend.common.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

val gson = Gson()
fun <T> apiFlow(
    call: suspend () -> Response<T>?
): Flow<ApiResult<T>> = flow {
    emit(ApiResult.Loading)
    try {
        Log.e("ApiFlow", "apiFlow")
        val c = call()
        c?.let {
            Log.e("ApiFlow", "apiFlow c: ${c.isSuccessful}")
            if (c.isSuccessful) {
                Log.e("ApiFlow", "apiFlow c.body: ${c.body()}")
                emit(ApiResult.Success(c.body()))
            } else {
                val errorBody = c.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, ApiErrorResponse::class.java)
                emit(ApiResult.Error(errorResponse.error))
            }
        }
    } catch (e: Exception) {
        Log.e("ApiFlow", e.message ?: "An error occurred")
        emit(ApiResult.Error(e.message ?: "An error occurred"))
    }
}.flowOn(Dispatchers.IO)



data class ApiErrorResponse(
    val error: String
)