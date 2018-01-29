package com.kiwi.flightoffers.dagger.component

import android.support.v4.app.Fragment
import com.kiwi.flightoffers.api.ImageApi
import com.kiwi.flightoffers.api.SkypickerApi
import com.kiwi.flightoffers.dagger.UIScope
import com.kiwi.flightoffers.ui.BaseActivity
import dagger.Component

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */

@UIScope
@Component(dependencies = arrayOf(AppComponent::class))
interface BaseUIComponent {

    fun skypickerApi(): SkypickerApi
    fun imageApi(): ImageApi

    fun inject(activity: BaseActivity)

    fun inject(fragment: Fragment)
}