package com.kiwi.flightoffers.dagger.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */

@Module
class GsonModule {

    @Provides
    @Singleton
    fun providesGson() = GsonBuilder().create()

    @Provides
    @Singleton
    fun providesGsonConverterFactory(gson : Gson) = GsonConverterFactory.create(gson)

}