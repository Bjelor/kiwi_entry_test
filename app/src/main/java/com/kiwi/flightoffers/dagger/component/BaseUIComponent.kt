package com.kiwi.flightoffers.dagger.component

import com.kiwi.flightoffers.dagger.UIScope
import com.kiwi.flightoffers.ui.BaseActivity
import com.kiwi.flightoffers.ui.BaseFragment
import dagger.Component

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */

@UIScope
@Component(dependencies = arrayOf(AppComponent::class))
interface BaseUIComponent {

    fun inject(activity: BaseActivity)

    fun inject(fragment: BaseFragment)
}