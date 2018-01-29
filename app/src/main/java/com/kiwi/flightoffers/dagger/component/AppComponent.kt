package com.kiwi.flightoffers.dagger.component

import android.app.Application
import android.content.res.Resources
import com.google.gson.Gson
import com.kiwi.flightoffers.api.Api
import com.kiwi.flightoffers.dagger.module.*
import dagger.Component
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, RetrofitModule::class, OkHttpModule::class, ApiModule::class, GsonModule::class))
interface AppComponent {
    fun application(): Application

    fun resources(): Resources

    fun api(): Api

    fun retrofit(): Retrofit
    fun gson(): Gson
    fun cache(): Cache
    fun client(): OkHttpClient
    fun loggingInterceptor(): HttpLoggingInterceptor
}