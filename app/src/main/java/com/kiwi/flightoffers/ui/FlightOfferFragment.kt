package com.kiwi.flightoffers.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kiwi.flightoffers.R
import com.kiwi.flightoffers.api.ImageAPIUtils
import com.kiwi.flightoffers.model.Flight
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
        if(!iata.isNullOrBlank()) {
            val logoUrl = ImageAPIUtils.getFlightLogoUrl(iata!!)
            Glide.with(context!!).load(logoUrl).into(view.airline_icon)
        }

        val mapIdto = arguments?.getString(ARG_IDTO)
        if(!mapIdto.isNullOrBlank()) {
            val imageUrl = ImageAPIUtils.getDestinationImageUrl(mapIdto!!)
            Glide.with(context!!).load(imageUrl).into(view.cover)
        }
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
        private val ARG_IDTO = "mapIdto"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(flight: Flight): FlightOfferFragment {
            val fragment = FlightOfferFragment()
            val args = Bundle()
            args.putString(ARG_LABEL, flight.cityTo)
            args.putString(ARG_PRICE, flight.price)
            args.putString(ARG_FROM, flight.cityFrom)
            args.putString(ARG_IATA, flight.flyFrom)
            args.putString(ARG_IDTO, flight.mapIdto)
            fragment.arguments = args
            return fragment
        }
    }
}