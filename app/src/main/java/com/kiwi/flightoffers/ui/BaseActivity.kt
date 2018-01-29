package com.kiwi.flightoffers.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    fun getAppcomponent(): AppComponent = App.appComponent

    private fun inject() {
        DaggerBaseUIComponent.builder()
                .appComponent(getAppcomponent())
                .build()
                .inject(this)
    }
}