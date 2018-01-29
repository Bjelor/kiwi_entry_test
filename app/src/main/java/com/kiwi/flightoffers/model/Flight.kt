package com.kiwi.flightoffers.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
data class Flight (
        val id: String,
        val countryTo: Country,
        val countryFrom: Country,
        val cityFrom: String,
        val cityTo: String,
        val price: String,
        val aTimeUTC: String,
        val dTimeUTC: String
) : Serializable