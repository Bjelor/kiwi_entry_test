package com.kiwi.flightoffers.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kiwi.flightoffers.App
import com.kiwi.flightoffers.api.Api
import com.kiwi.flightoffers.dagger.component.AppComponent
import com.kiwi.flightoffers.dagger.component.DaggerAppComponent
import javax.inject.Inject

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    fun getAppcomponent(): AppComponent = App.appComponent

    private fun inject() {
        DaggerBaseActivityComponent.builder()
                .appComponent(getAppcomponent())
                .build()
                .inject(this)
    }
}