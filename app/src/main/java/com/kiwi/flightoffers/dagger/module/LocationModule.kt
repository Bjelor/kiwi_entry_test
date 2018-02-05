package com.kiwi.flightoffers.dagger.module

import android.location.LocationManager
import dagger.Module
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