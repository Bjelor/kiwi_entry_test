package com.kiwi.flightoffers.model.utils

import android.content.Context
import com.kiwi.flightoffers.R
import com.kiwi.flightoffers.model.CollectionResponse
import com.kiwi.flightoffers.model.Flight

/**
 * @author Bjelis
 * @since 4. 2. 2018
 */

fun CollectionResponse<Flight>.getCurrencyAsSymbol(context : Context) : String{
    return when(currency) {
        "EUR" -> context.resources.getString(R.string.currency_eur)
        "GBP" -> context.resources.getString(R.string.currency_pound)
        "USD" -> context.resources.getString(R.string.currency_dollar)
        else -> context.resources.getString(R.string.currency_eur)
    }
}
