package com.kiwi.flightoffers.ui

import com.kiwi.flightoffers.dagger.ActivityScope
import com.kiwi.flightoffers.dagger.component.AppComponent
import dagger.Component

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */

@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class))
interface BaseActivityComponent {

    fun inject(activity: BaseActivity)
}