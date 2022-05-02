package com.geekbrains.myweather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.geekbrains.myweather.databinding.FragmentDetailsBinding
import com.geekbrains.myweather.model.Weather
import com.geekbrains.myweather.utils.KEY_BUNDLE_WEATHER
import com.geekbrains.myweather.viewmodel.DetailsState
import com.geekbrains.myweather.viewmodel.DetailsViewModel
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
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner, object: Observer<DetailsState>{
            override fun onChanged(t: DetailsState) {
                renderData(t)
            }
        })
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            viewModel.getWeather(it.city)
        }
    }

    private fun renderData(detailsState: DetailsState) {
        when(detailsState){
            is DetailsState.Error ->{

            }
            is DetailsState.Loading ->{

            }
            is DetailsState.Success ->{
                val weather = detailsState.weather
                with(binding) {
                    loadingLayout.visibility = View.GONE
                    temperatureValue.text = weather.temperature.toString()
                    cityName.text = weather.city.name
                    feelsLikeValue.text = weather.feelsLike.toString()
                    cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
                    Snackbar.make(mainView, "Success", Snackbar.LENGTH_SHORT).show()
                    // Добавил иконку состояния погоды
                    icon.loadSvg("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
                }
            }
        }

    }

    private fun AppCompatImageView.loadSvg(url: String){
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry {add(SvgDecoder(this@loadSvg.context))}
            .build()
        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}