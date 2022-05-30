package com.geekbrains.myweather.lesson10

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.myweather.R
import com.geekbrains.myweather.databinding.FragmentMapsYandexMainBinding
import com.geekbrains.myweather.databinding.FragmentWeatherListBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView


class MapsFragmentYandex : Fragment() {

    private var _binding: FragmentMapsYandexMainBinding? = null
    private val binding: FragmentMapsYandexMainBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val mapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitInitializer.initialize("bd728ed0-e958-416a-b5c5-b737030fc078", requireContext())
        _binding = FragmentMapsYandexMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapview = childFragmentManager.findFragmentById(R.id.mapview) as MapView?
        mapview?.map?.move(
            CameraPosition(Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F), null)
    }

    object MapKitInitializer {
        private var initialized = false
        fun initialize(apiKey: String, context: Context) {
            if (initialized) {
                return
            }
            MapKitFactory.setApiKey(apiKey)
            MapKitFactory.initialize(context)
            initialized = true
        }
    }

    override fun onStop() {
        mapView?.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView?.onStart()
    }
//    val locationManager = MapKitFactory.getInstance().createLocationManager()
//    locationManager!!.requestSingleUpdate(object : LocationListener {
//
//        override fun onLocationUpdated(p0: Location) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onLocationStatusUpdated(p0: LocationStatus) {
//            TODO("Not yet implemented")
//        }
//    })
}