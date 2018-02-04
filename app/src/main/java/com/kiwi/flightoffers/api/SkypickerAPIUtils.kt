package com.kiwi.flightoffers.api

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Bjelis
 * @since 4. 2. 2018
 */
class SkypickerAPIUtils {
    companion object {
        private val API_DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        fun getCurrentDateString() : String = API_DATE_FORMAT.format(Calendar.getInstance().time)

        fun getNextMonthString() : String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, 1)

            return API_DATE_FORMAT.format(calendar.time)
        }
    }
}