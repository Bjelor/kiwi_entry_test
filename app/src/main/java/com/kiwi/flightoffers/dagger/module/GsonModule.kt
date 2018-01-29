package com.kiwi.flightoffers.dagger.module

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.kiwi.flightoffers.model.Flight
import dagger.Module
import dagger.Provides
import java.lang.reflect.Type
import javax.inject.Singleton

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */

@Module
class GsonModule {

    @Provides
    @Singleton
    fun providesGson() = GsonBuilder().create()

//
//    class FlightDeserializer : JsonDeserializer<Flight> {
//
//        companion object {
//            private const val FIELD_ID = "id"
//        }
//
//        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Flight? {
//            var jo = json?.asJsonObject
//
//            jo?.let {
//            if (!jo.has(FIELD_ID))
//                return null
//
//                val id : String = jo.get(FIELD_ID).asString
//
//
//
//            }
//            return null
//        }
//
//    }
}