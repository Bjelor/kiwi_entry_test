package com.kiwi.flightoffers.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.kiwi.flightoffers.R
import com.kiwi.flightoffers.model.CollectionResponse
import com.kiwi.flightoffers.model.Flight
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class MainActivity : BaseActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private const val ARG_FLIGHTS = "flights"
    }


    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        savedInstanceState?.let {
            mSectionsPagerAdapter?.flights = savedInstanceState.get(ARG_FLIGHTS) as List<Flight>
        }

        if (mSectionsPagerAdapter?.flights?.isEmpty() == true) {
            showLoading()
            skypickerApi.getFlightOffers().enqueue(object : Callback<CollectionResponse<Flight>> {
                override fun onResponse(call: Call<CollectionResponse<Flight>>, response: Response<CollectionResponse<Flight>>) {
                    Log.d(TAG, response.toString())
                    hideLoading()
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

        var flights: List<Flight> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a FlightOfferFragment (defined as a static inner class below).
            val flight = flights[position]

            return FlightOfferFragment.newInstance(flight.cityTo, flight.price, flight.cityFrom, flight.flyFrom)
        }

        override fun getCount(): Int {
            return flights.size
        }


    }


}
