package com.kiwi.flightoffers.dagger.module

import dagger.Module
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.content.Context.LOCATION_SERVICE
import javax.inject.Inject


/**
 * @author Bjelis
 * @since 4. 2. 2018
 */
@Module
class LocationModule {

    @Inject
    lateinit var locationManager : LocationManager


}