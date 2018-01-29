package com.kiwi.flightoffers.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.kiwi.flightoffers.App
import com.kiwi.flightoffers.api.ImageApi
import com.kiwi.flightoffers.api.SkypickerApi
import com.kiwi.flightoffers.dagger.component.AppComponent
import com.kiwi.flightoffers.dagger.component.DaggerBaseUIComponent
import javax.inject.Inject

/**
 * @author adamb_000
 * @since 29. 1. 2018
 */
abstract class BaseFragment : Fragment() {

    @Inject
    protected lateinit var skypickerApi: SkypickerApi

    @Inject
    protected lateinit var imageApi: ImageApi

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