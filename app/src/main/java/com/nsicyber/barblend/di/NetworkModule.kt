package com.nsicyber.barblend.di

import com.nsicyber.barblend.common.ApiKeyProvider
import com.nsicyber.barblend.common.Constants
import com.nsicyber.barblend.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = (HttpLoggingInterceptor.Level.BODY)
            },
        ).addInterceptor(
            Interceptor { chain ->
                val request =
                    chain.request().newBuilder()
                        .addHeader(Constants.OkHttpKeys.API_KEY_HEADER, ApiKeyProvider.getApiKey())
                        .addHeader(
                            Constants.OkHttpKeys.API_HOST_HEADER,
                            Constants.OkHttpKeys.API_HOST_VALUE,
                        )
                        .build()
                chain.proceed(request)
            },
        ).build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService =
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
            .create(ApiService::class.java)
}
