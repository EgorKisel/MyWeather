package com.geekbrains.myweather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.myweather.R
import com.geekbrains.myweather.viewmodel.MainViewModel

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //val observer = Observer<Any>{renderData(it)}
        val observer = object:Observer<Any>{
            override fun onChanged(data: Any?) {
                if (data != null) {
                    renderData(data)
                }
            }
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getWeather()
    }

    private fun renderData(data: Any){
        Toast.makeText(requireContext(), "РАБОТАЕТ", Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}