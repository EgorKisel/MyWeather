package com.geekbrains.myweather.view.weatherlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.myweather.R
import com.geekbrains.myweather.databinding.FragmentWeatherListBinding
import com.geekbrains.myweather.model.Weather
import com.geekbrains.myweather.utils.KEY_BUNDLE_WEATHER
import com.geekbrains.myweather.view.details.DetailsFragment
import com.geekbrains.myweather.viewmodel.AppState
import com.geekbrains.myweather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

private const val IS_WORLD_KEY = "IS_WORLD_KEY"

class WeatherListFragment : Fragment(), OnItemListClickListener {

    private var isRussian = true
    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }

    private val adapter = WeatherListAdapter(this)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter

        binding.floatingActionButton.setOnClickListener {
            changeWeatherDataSet()
        }

        val observer = Observer<AppState> { data -> renderData(data) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        showListOfTowns()

    }

    private fun changeWeatherDataSet() {
        if (isRussian) {
            viewModel.getWeatherWorld()
            binding.floatingActionButton.setImageResource(R.drawable.ic_earth)
        } else {
            viewModel.getWeatherRussia()
            binding.floatingActionButton.setImageResource(R.drawable.ic_russia)
        }.also { isRussian = !isRussian }

        saveListOfTowns()
    }

    private fun showListOfTowns(){
        activity?.let {
            if(it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY, false)) {
                changeWeatherDataSet()
            } else {
                viewModel.getWeatherRussia()
            }
        }
    }

    private fun saveListOfTowns(){
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()){
                putBoolean(IS_WORLD_KEY, !isRussian)
                apply()
            }
        }
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.root, "Не получилось ${data.error}", Snackbar.LENGTH_SHORT)
                    .show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherList)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.container, DetailsFragment.newInstance(Bundle().apply {
                putParcelable(KEY_BUNDLE_WEATHER, weather)
            })
        ).addToBackStack("").commit()
    }
}