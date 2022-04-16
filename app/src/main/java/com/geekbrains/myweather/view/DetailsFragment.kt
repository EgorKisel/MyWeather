package com.geekbrains.myweather.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.myweather.databinding.FragmentDetailsBinding
import com.geekbrains.myweather.databinding.FragmentWeatherListBinding
import com.geekbrains.myweather.model.Weather
import com.geekbrains.myweather.utils.KEY_BUNDLE_WEATHER
import com.geekbrains.myweather.viewmodel.AppState
import com.geekbrains.myweather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment() {

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
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather: Weather = requireArguments().getParcelable<Weather>(KEY_BUNDLE_WEATHER)!!
        renderData(weather)
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(weather: Weather){
        binding.loadingLayout.visibility = View.GONE
        binding.temperatureValue.text = weather.temperature.toString()
        binding.cityName.text = weather.city.name.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        binding.cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
        Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment{
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}