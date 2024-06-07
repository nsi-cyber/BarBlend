package com.nsicyber.barblend.data.remote

import com.nsicyber.barblend.common.ApiKeyProvider
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request().newBuilder()
            .addHeader("X-RapidAPI-Key", ApiKeyProvider.getApiKey())
            .addHeader("X-RapidAPI-Host", "the-cocktail-db.p.rapidapi.com")
            .build()
        return chain.proceed(request)
    }
}