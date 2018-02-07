package com.kiwi.flightoffers.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import com.google.gson.reflect.TypeToken
import com.kiwi.flightoffers.R
import com.kiwi.flightoffers.api.SkypickerAPIUtils
import com.kiwi.flightoffers.model.CollectionResponse
import com.kiwi.flightoffers.model.Flight
import com.kiwi.flightoffers.model.utils.getCurrencyAsSymbol
import com.kiwi.flightoffers.utils.getApiLocationString
import com.kiwi.flightoffers.utils.getBestProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.error_main.*
import kotlinx.android.synthetic.main.loading_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class MainActivity : BaseActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private const val LOCATION_PERMISSION_REQUEST_CODE = 555

        private const val FLIGHTS_COUNT = 5

        private const val ARG_FLIGHTS = "flights"
        private const val ARG_CURRENCY = "currency"
        private const val ARG_LAST_DATE = "last_date"
        private const val ARG_IS_LOCATION_READY = "is_location_ready"

    }

    private var flightsPagerAdapter: SectionsPagerAdapter? = null

    private var lastDate: Long = 0

    private var isLocationReady = false

    private val locationTimeoutHandler = Handler()

    private var lastKnownLocation : Location? = null

    private val locationTimeoutRunnable: Runnable = Runnable {
        if (!isLocationReady) {
            locationManager.removeUpdates(locationListener)
            loadNewFlights(lastKnownLocation)
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            Log.d(TAG, "onLocationChanged")
            isLocationReady = true
            locationTimeoutHandler.removeCallbacks(locationTimeoutRunnable)
            loadNewFlights(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            Log.d(TAG, "onStatusChanged")
        }

        override fun onProviderEnabled(provider: String?) {
            Log.d(TAG, "onProviderEnabled")
        }

        override fun onProviderDisabled(provider: String?) {
            Log.d(TAG, "onProviderDisabled")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initPager()

        if (savedInstanceState != null) {
            restoreFromSavedInstanceState(savedInstanceState)
        } else if (sharedPreferences.getString(ARG_FLIGHTS, null) != null) {
            restoreFromSharedPreferences()
        }

        if (areCurrentFlightsInvalid()) {
            tryRequestingCurrentLocation()
        }

        fab.setOnClickListener { view ->
            if (lastDate > 0)
                Snackbar.make(view, "Flights updated on " + SkypickerAPIUtils.getDateString(lastDate), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
        }
    }

    private fun tryRequestingCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val bestProvider = locationManager.getBestProvider()

            if (bestProvider == null)
                loadNewFlights()
            else {
                showLoading(getString(R.string.loading_location))
                locationManager.requestSingleUpdate(bestProvider, locationListener, null)

                val timeout: Long = 100000

                lastKnownLocation = locationManager.getLastKnownLocation(bestProvider)

                Handler().postAtTime(
                        locationTimeoutRunnable,
                        timeout
                )
            }
        } else
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE)
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tryRequestingCurrentLocation()
            } else {
                loadNewFlights()
            }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun loadNewFlights(location : Location? = null) {
        showLoading()
        val currentDate = SkypickerAPIUtils.getCurrentDateString()
        skypickerApi.getFlightOffers(currentDate, currentDate, location.getApiLocationString()).enqueue(object : Callback<CollectionResponse<Flight>> {
            override fun onResponse(call: Call<CollectionResponse<Flight>>, response: Response<CollectionResponse<Flight>>) {
                Log.d(TAG, response.toString())
                hideLoading()
                hideError()

                lastDate = SkypickerAPIUtils.getCurrentDateLong()
                val currency = response.body().getCurrencyAsSymbol(this@MainActivity)
                val newFlights = response.body().data
                val flights = filterFlightsAndSave(newFlights, currency)

                if (flights.isNotEmpty()) {
                    fillPagerWithData(currency, flights)
                    flight_pager.currentItem = 0
                } else {
                    showError(getString(R.string.no_new_flights))
                }
            }

            override fun onFailure(call: Call<CollectionResponse<Flight>>, t: Throwable) {
                Log.e(TAG, t.message)
                hideLoading()
                showError()
            }
        })
    }

    private fun areCurrentFlightsInvalid() =
            flightsPagerAdapter?.flights?.isEmpty() == true || !SkypickerAPIUtils.isDateToday(lastDate)

    private fun restoreFromSharedPreferences() {
        var flights: List<Flight> = gson.fromJson(sharedPreferences.getString(ARG_FLIGHTS, null), object : TypeToken<List<Flight>>() {}.type)
        if (flights.size > FLIGHTS_COUNT)
            flights = flights.subList(flights.size - FLIGHTS_COUNT, flights.size)
        val currency = sharedPreferences.getString(ARG_CURRENCY, null)
        lastDate = sharedPreferences.getLong(ARG_LAST_DATE, 0)
        fillPagerWithData(currency, flights)
    }

    private fun restoreFromSavedInstanceState(savedInstanceState: Bundle) {
        val flights: List<Flight> = savedInstanceState.get(ARG_FLIGHTS) as List<Flight>
        val currency = savedInstanceState.getString(ARG_CURRENCY)
        lastDate = savedInstanceState.getLong(ARG_LAST_DATE)
        isLocationReady = savedInstanceState.getBoolean(ARG_IS_LOCATION_READY, false)
        fillPagerWithData(currency, flights)
    }

    private fun filterFlightsAndSave(newFlights: List<Flight>, currency: String): List<Flight> {
        val oldFlights: MutableList<Flight> = gson.fromJson(sharedPreferences.getString(ARG_FLIGHTS, null), object : TypeToken<MutableList<Flight>>() {}.type)
                ?: mutableListOf()
        val flights: MutableList<Flight> = mutableListOf()

        if (oldFlights.isNotEmpty()) {
            var index = 0
            while (flights.size < FLIGHTS_COUNT && index < newFlights.size) {
                val flight = newFlights[index]
                if (!oldFlights.contains(flight) && !flights.contains(flight)) {
                    flights.add(flight)
                    oldFlights.add(flight)
                }
                index++
            }
        } else {
            val index = if (newFlights.size > FLIGHTS_COUNT) FLIGHTS_COUNT else newFlights.lastIndex
            flights.addAll(newFlights.subList(0, index))
            oldFlights.addAll(flights)
        }

        saveResponseInfo(oldFlights, currency, lastDate)
        return flights.toList()
    }

    private fun saveResponseInfo(flights: List<Flight>, currency: String, lastDate: Long) {
        sharedPreferences.edit()
                .putString(ARG_FLIGHTS, gson.toJson(flights).toString())
                .putString(ARG_CURRENCY, currency)
                .putLong(ARG_LAST_DATE, lastDate)
                .apply()
    }

    private fun initPager() {
        flightsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        flight_pager.adapter = flightsPagerAdapter
        val pageChangeListener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                //do nothing
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //do nothing
            }

            override fun onPageSelected(position: Int) {
                val flight = flightsPagerAdapter?.flights?.get(position)
                flight?.let {
                    fillBottomTextViews(it.fly_duration, it.distance)
                }
            }
        }

        pager_indicator?.setViewPager(flight_pager)
        pager_indicator?.setOnPageChangeListener(pageChangeListener)

    }

    private fun fillPagerWithData(currency: String, flights: List<Flight>) {
        flightsPagerAdapter?.currency = currency
        flightsPagerAdapter?.flights = flights
        val durationValue = flightsPagerAdapter?.flights?.get(flight_pager.currentItem)?.fly_duration
        val distanceValue = flightsPagerAdapter?.flights?.get(flight_pager.currentItem)?.distance
        fillBottomTextViews(durationValue, distanceValue)
    }

    private fun fillBottomTextViews(durationValue: String?, distanceValue: String?) {
        duration?.text = durationValue
        distance?.text = getString(R.string.distance, distanceValue)
    }

    private fun showLoading(message: String? = null) {
        loading_container.visibility = View.VISIBLE
        loading_message.text = message
        fab.visibility = View.GONE
    }

    private fun hideLoading() {
        loading_container.visibility = View.GONE
        loading_message.text = null
        fab.visibility = View.VISIBLE
    }

    private fun showError(message: String = getString(R.string.error)) {
        error_message.text = message
        error_container.visibility = View.VISIBLE
        fab.visibility = View.GONE
    }

    private fun hideError() {
        error_container.visibility = View.GONE
        fab.visibility = View.VISIBLE
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        flightsPagerAdapter?.let {
            outState?.putSerializable(ARG_FLIGHTS, it.flights.toList() as Serializable)
            outState?.putString(ARG_CURRENCY, it.currency)
            outState?.putLong(ARG_LAST_DATE, lastDate)
            outState?.putBoolean(ARG_IS_LOCATION_READY, isLocationReady)
        }
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        var currency: String = resources.getString(R.string.currency_eur)
        var flights: List<Flight> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getItem(position: Int): Fragment {
            val flight = flights[position]
            return FlightOfferFragment.newInstance(flight, currency)
        }

        override fun getCount(): Int {
            return flights.size
        }
    }
}
