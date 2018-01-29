package com.kiwi.flightoffers.dagger.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
@Module
class RetrofitModule {
    companion object {
        private const val BASE_URL = "https://api.skypicker.com/"
    }

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
}