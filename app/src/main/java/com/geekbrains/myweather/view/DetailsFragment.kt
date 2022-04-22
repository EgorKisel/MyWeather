package com.geekbrains.myweather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.myweather.databinding.FragmentDetailsBinding
import com.geekbrains.myweather.model.OnServerResponse
import com.geekbrains.myweather.model.Weather
import com.geekbrains.myweather.model.WeatherDTO
import com.geekbrains.myweather.model.WeatherLoader
import com.geekbrains.myweather.utils.KEY_BUNDLE_WEATHER
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment(), OnServerResponse {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var currentCityName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            Thread {
                WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)
            }.start()
        }
    }

    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            loadingLayout.visibility = View.GONE
            temperatureValue.text = weather.factDTO.temperature.toString()
            cityName.text = currentCityName
            feelsLikeValue.text = weather.factDTO.feelsLike.toString()
            cityCoordinates.text = "${weather.infoDTO.lat} ${weather.infoDTO.lon}"
        }
        mainView.showSnackBar()
    }

    private fun View.showSnackBar() {
        Snackbar.make(mainView, "Получилось", Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }
}