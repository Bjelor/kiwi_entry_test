package com.kiwi.flightoffers.api

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Bjelis
 * @since 4. 2. 2018
 */
class SkypickerAPIUtils {
    companion object {
        private val API_DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        fun isDateToday(time : Long) : Boolean = DateUtils.isToday(time)

        fun getCurrentDateLong() : Long = Calendar.getInstance().time.time

        fun getCurrentDateString() : String = API_DATE_FORMAT.format(Calendar.getInstance().time)

        fun getDateString(time : Long) : String = API_DATE_FORMAT.format(time)

        fun getNextMonthString() : String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, 1)

            return API_DATE_FORMAT.format(calendar.time)
        }
    }
}