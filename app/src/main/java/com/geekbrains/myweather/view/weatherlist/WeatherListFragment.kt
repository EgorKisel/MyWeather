package com.geekbrains.myweather.view.weatherlist

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.myweather.R
import com.geekbrains.myweather.databinding.FragmentWeatherListBinding
import com.geekbrains.myweather.model.City
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
        setupFabLocation()
        showListOfTowns()
    }

    private fun setupFabLocation(){
        binding.mainFragmentFABLocation.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        }else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
            explain()
        }else{
            mRequestPermission()
        }
    }

    private fun explain(){
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_rationale_title))
            .setMessage(getString(R.string.dialog_rationale_message))
            .setPositiveButton(getString(R.string.dialog_rationale_give_access)){_, _ ->
                mRequestPermission()
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)){dialog, _ -> dialog.dismiss()}
            .create()
            .show()
    }

    private val REQUEST_CODE = 989
    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==999){
            for (i in permissions.indices){
                if (permissions[i]==Manifest.permission.ACCESS_FINE_LOCATION&&grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    getLocation()
                }else{
                    explain()
                }
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private val locationListener = object : LocationListener{
        override fun onLocationChanged(location: Location) {
            Log.d("@@@", location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }
    }

    fun getAddressByLocation(location: Location){
        val geocoder = Geocoder(requireContext())
        val timeStump = System.currentTimeMillis()
        Thread{
            val addressText = geocoder.getFromLocation(location.latitude, location.longitude, 3)[0].getAddressLine(0)
            requireActivity().runOnUiThread{
                showAddressDialog(addressText, location)
            }
        }.start()

        Log.d("@@@", "прошло ${System.currentTimeMillis() - timeStump}")
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                providerGPS?.let {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        10000L,
                        100f,
                        locationListener
                    )
                }
            }
        }
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

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    onItemClick(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }
}