package com.kiwi.flightoffers.model

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
data class CollectionResponse<T>(var currency : String, var data : List<T>)