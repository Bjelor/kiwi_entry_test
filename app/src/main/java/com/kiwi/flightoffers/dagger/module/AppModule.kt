package com.kiwi.flightoffers.dagger.module

import android.app.Application
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */

@Module
class AppModule(val application: Application)  {

    @Provides
    @Singleton
    fun providesApplication() = application

    @Provides
    @Singleton
    fun providesResources() = application.resources

}