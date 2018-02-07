package com.kiwi.flightoffers.api

/**
 * @author Bjelis
 * @since 30. 1. 2018
 */
class ImageAPIUtils {
    companion object {
        fun getFlightLogoUrl(iata: String) : String = ImageApi.BASE_URL + "airlines/64/$iata.png"

        fun getDestinationImageUrl(mapIdto: String) : String = ImageApi.BASE_URL + "photos/600/$mapIdto.jpg"
    }
}