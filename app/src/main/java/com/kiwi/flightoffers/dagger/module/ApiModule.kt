package com.kiwi.flightoffers.dagger.module

import com.kiwi.flightoffers.api.ImageApi
import com.kiwi.flightoffers.api.SkypickerApi
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
class ApiModule {

    @Provides
    @Singleton
    fun providesSkypickerApi(client: OkHttpClient, gsonConverterFactory: GsonConverterFactory): SkypickerApi = Retrofit.Builder().baseUrl(SkypickerApi.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build().create(SkypickerApi::class.java)

    @Provides
    @Singleton
    fun providesImageApi(client: OkHttpClient, gsonConverterFactory: GsonConverterFactory): ImageApi = Retrofit.Builder().baseUrl(ImageApi.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build().create(ImageApi::class.java)
}