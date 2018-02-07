package com.kiwi.flightoffers.api

import com.kiwi.flightoffers.model.CollectionResponse
import com.kiwi.flightoffers.model.Flight
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
interface SkypickerApi {

    companion object {
        const val BASE_URL = "https://api.skypicker.com/"

        const val DEFAULT_COORDINATES = "49.2-16.61-250km"
    }

    @GET("flights?v=2&sort=popularity&asc=0&locale=en&daysInDestinationFrom=&daysInDestinationTo=&affilid=&children=0&infants=0&to=anywhere&featureName=aggregateResults&typeFlight=oneway&returnFrom=&returnTo=&one_per_date=0&oneforcity=1&wait_for_refresh=0&adults=1&limit=45")
    fun getFlightOffers(
            @Query("dateFrom") dateFrom : String,
            @Query("dateTo") dateTo : String,
            @Query("flyFrom") from : String = DEFAULT_COORDINATES) : Call<CollectionResponse<Flight>>
}