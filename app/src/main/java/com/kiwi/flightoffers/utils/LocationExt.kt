package com.kiwi.flightoffers.utils

import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import com.kiwi.flightoffers.api.SkypickerApi

/**
 * @author adamb_000
 * @since 7. 2. 2018
 */

fun LocationManager.getBestProvider() : String? {
    val criteria = Criteria()
    criteria.accuracy = Criteria.NO_REQUIREMENT
    criteria.powerRequirement = Criteria.NO_REQUIREMENT

    return getBestProvider(criteria, false)
}

fun Location?.getApiLocationString() : String {
    this?.let {
        return String.format("%.2f",latitude) + "-" + String.format("%.2f",longitude) + "-250km"
    }
    return SkypickerApi.DEFAULT_COORDINATES
}
