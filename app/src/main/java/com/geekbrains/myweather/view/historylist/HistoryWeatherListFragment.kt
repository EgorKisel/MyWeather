package com.geekbrains.myweather.view.weatherlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.myweather.databinding.FragmentHistoryWeatherListBinding
import com.geekbrains.myweather.viewmodel.AppState
import com.geekbrains.myweather.viewmodel.HistoryViewModel
import com.geekbrains.myweather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class HistoryWeatherListFragment : Fragment() {

    private var isRussian = true
    private var _binding: FragmentHistoryWeatherListBinding? = null
    private val binding: FragmentHistoryWeatherListBinding
        get() {
            return _binding!!
        }

    private val adapter = HistoryWeatherListAdapter()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter

        val observer = Observer<AppState> { data -> renderData(data) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getAll()
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                //binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.root, "Не получилось ${data.error}", Snackbar.LENGTH_SHORT)
                    .show()
            }
            is AppState.Loading -> {
               // binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                //binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherList)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryWeatherListFragment()
    }

}