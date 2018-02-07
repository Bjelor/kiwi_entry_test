package com.kiwi.flightoffers.ui

import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.kiwi.flightoffers.App
import com.kiwi.flightoffers.api.ImageApi
import com.kiwi.flightoffers.api.SkypickerApi
import com.kiwi.flightoffers.dagger.component.AppComponent
import com.kiwi.flightoffers.dagger.component.DaggerBaseUIComponent
import javax.inject.Inject

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
abstract class BaseActivity : AppCompatActivity() {

    @Inject
    protected lateinit var skypickerApi: SkypickerApi

    @Inject
    protected lateinit var imageApi: ImageApi

    @Inject
    protected lateinit var sharedPreferences: SharedPreferences

    @Inject
    protected lateinit var gson: Gson

    @Inject
    protected lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    fun getAppComponent(): AppComponent = App.appComponent

    private fun inject() {
        DaggerBaseUIComponent.builder()
                .appComponent(getAppComponent())
                .build()
                .inject(this)
    }
}