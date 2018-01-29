package com.kiwi.flightoffers.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kiwi.flightoffers.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author adamb_000
 * @since 29. 1. 2018
 */
/**
 * A placeholder fragment containing a simple view.
 */
class FlightOfferFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        rootView.title.text = arguments?.getString(ARG_LABEL)
        rootView.price.text = resources.getString(R.string.price_format, arguments?.getString(ARG_PRICE))
        rootView.from.text = resources.getString(R.string.from_format, arguments?.getString(ARG_FROM))
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val iata = arguments?.getString(ARG_IATA)

        if(airline_icon.drawable == null && !iata.isNullOrBlank())
            imageApi.getFlightLogo(iata!!).enqueue(object : Callback<String> {

                override fun onResponse(call: Call<String>?, response: Response<String>?) {

                    val url = response?.body()
                    if(!url.isNullOrEmpty() && isAdded && context != null)
                        Glide.with(context!!).load(url).into(view.cover)
                    else
                        view.cover.visibility = View.INVISIBLE
                }

                override fun onFailure(call: Call<String>?, t: Throwable?) {
                    Log.e(FlightOfferFragment.TAG, t?.message)
                    view.cover.visibility = View.INVISIBLE
                }

            })
    }

    companion object {

        private val TAG = FlightOfferFragment::class.java.simpleName
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_LABEL = "label"
        private val ARG_PRICE = "price"
        private val ARG_FROM = "from"
        private val ARG_IATA = "iata"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(label: String, price: String, from: String, iata: String): FlightOfferFragment {
            val fragment = FlightOfferFragment()
            val args = Bundle()
            args.putString(ARG_LABEL, label)
            args.putString(ARG_PRICE, price)
            args.putString(ARG_FROM, from)
            args.putString(ARG_IATA, iata)
            fragment.arguments = args
            return fragment
        }
    }
}