package com.kiwi.flightoffers.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.kiwi.flightoffers.R
import com.kiwi.flightoffers.api.SkypickerAPIUtils
import com.kiwi.flightoffers.model.CollectionResponse
import com.kiwi.flightoffers.model.Flight
import com.kiwi.flightoffers.model.utils.getCurrencyAsSymbol
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.loading_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class MainActivity : BaseActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private const val ARG_FLIGHTS = "flights"
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        flightPager.adapter = mSectionsPagerAdapter
        flightPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                //do nothing
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //do nothing
            }

            override fun onPageSelected(position: Int) {
                val flight = mSectionsPagerAdapter?.flights?.get(position)
                flight?.let {
                    duration.text = it.fly_duration
                }
            }
        })

        if(savedInstanceState != null)
            mSectionsPagerAdapter?.flights = savedInstanceState.get(ARG_FLIGHTS) as List<Flight>
        else if (sharedPreferences.getString(ARG_FLIGHTS, null) != null)
            mSectionsPagerAdapter?.flights = gson.fromJson(sharedPreferences.getString(ARG_FLIGHTS, null), object : TypeToken<List<Flight>>() {}.type)

        if (mSectionsPagerAdapter?.flights?.isEmpty() == true) {
            showLoading()
            val dateFrom = SkypickerAPIUtils.getCurrentDateString()
            val dateTo = SkypickerAPIUtils.getNextMonthString()
            skypickerApi.getFlightOffers(dateFrom, dateTo).enqueue(object : Callback<CollectionResponse<Flight>> {
                override fun onResponse(call: Call<CollectionResponse<Flight>>, response: Response<CollectionResponse<Flight>>) {
                    Log.d(TAG, response.toString())
                    hideLoading()
                    sharedPreferences.edit().putString(ARG_FLIGHTS, gson.toJson(response.body().data).toString()).apply()
                    mSectionsPagerAdapter?.currency = response.body().getCurrencyAsSymbol(this@MainActivity)
                    mSectionsPagerAdapter?.flights = response.body().data
                }

                override fun onFailure(call: Call<CollectionResponse<Flight>>, t: Throwable) {
                    Log.e(TAG, t.message)
                    hideLoading()
                }
            })
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }


    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mSectionsPagerAdapter?.let {
            outState?.putSerializable(ARG_FLIGHTS, mSectionsPagerAdapter!!.flights as Serializable)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        var currency : String = resources.getString(R.string.currency_eur)
        var flights: List<Flight> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }



        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a FlightOfferFragment (defined as a static inner class below).
            val flight = flights[position]

            return FlightOfferFragment.newInstance(flight, currency)
        }

        override fun getCount(): Int {
            return flights.size
        }


    }


}
