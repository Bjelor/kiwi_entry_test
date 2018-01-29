package com.kiwi.flightoffers.dagger.module

import com.kiwi.flightoffers.api.Api
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
@Module
class ApiModule {
    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}