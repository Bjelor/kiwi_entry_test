package com.kiwi.flightoffers.model

import java.io.Serializable

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
data class Flight (
        val id: String,
        val countryTo: Country,
        val countryFrom: Country,
        val flyFrom: String,
        val cityFrom: String,
        val cityTo: String,
        val mapIdto: String,
        val price: String,
        val currency: String,
        val fly_duration: String,
        val distance: String,
        val aTimeUTC: String,
        val dTimeUTC: String
) : Serializable {

    override fun equals(other: Any?): Boolean
        = if (other is Flight)
            other.mapIdto == this.mapIdto
        else
            false

}