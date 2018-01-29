package com.kiwi.flightoffers.dagger.component

import android.app.Application
import android.content.res.Resources
import com.google.gson.Gson
import com.kiwi.flightoffers.api.ImageApi
import com.kiwi.flightoffers.api.SkypickerApi
import com.kiwi.flightoffers.dagger.module.ApiModule
import com.kiwi.flightoffers.dagger.module.AppModule
import com.kiwi.flightoffers.dagger.module.GsonModule
import com.kiwi.flightoffers.dagger.module.OkHttpModule
import dagger.Component
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, OkHttpModule::class, ApiModule::class, GsonModule::class))
interface AppComponent {
    fun application(): Application

    fun resources(): Resources

    fun skypickerApi(): SkypickerApi
    fun imageApi(): ImageApi

    fun gson(): Gson
    fun cache(): Cache
    fun client(): OkHttpClient
    fun loggingInterceptor(): HttpLoggingInterceptor
}