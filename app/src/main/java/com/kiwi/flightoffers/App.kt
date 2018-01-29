package com.kiwi.flightoffers

import android.app.Application
import com.kiwi.flightoffers.dagger.component.AppComponent
import com.kiwi.flightoffers.dagger.component.DaggerAppComponent
import com.kiwi.flightoffers.dagger.module.AppModule

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
class App: Application() {

    companion object{
        @JvmStatic lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()

    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

}