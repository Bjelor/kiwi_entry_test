package com.kiwi.flightoffers.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author adamb_000
 * @since 29. 1. 2018
 */
interface ImageApi {

    companion object {
        const val BASE_URL = "https://images.kiwi.com/airlines/64/"
    }

    @GET("{iata}.png")
    fun getFlightLogo(@Path("iata") iata: String): Call<String>

}