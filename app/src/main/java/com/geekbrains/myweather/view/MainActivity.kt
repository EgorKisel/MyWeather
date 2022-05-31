package com.geekbrains.myweather.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.geekbrains.myweather.MyApp
import com.geekbrains.myweather.R
import com.geekbrains.myweather.lesson10.MapsFragment
import com.geekbrains.myweather.lesson10.MapsFragmentYandex
import com.geekbrains.myweather.lesson9.WorkWithContentProviderFragment
import com.geekbrains.myweather.model.MyBroadcastReceiver
import com.geekbrains.myweather.utils.KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN
import com.geekbrains.myweather.utils.KEY_SP_NAME_1
import com.geekbrains.myweather.view.weatherlist.HistoryWeatherListFragment
import com.geekbrains.myweather.view.weatherlist.WeatherListFragment


class MainActivity : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance())
                .commit()
        }

        val receiver = MyBroadcastReceiver()
        registerReceiver(receiver, IntentFilter("android.intent.action.AIRPLANE_MODE"))

        val sp = getSharedPreferences(KEY_SP_NAME_1, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, true)
        editor.apply()

        MyApp.getHistoryDao().getAll()

        val defaultValueIsRussian = true
        sp.getBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, defaultValueIsRussian)

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, HistoryWeatherListFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            R.id.action_work_with_content_provider -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, WorkWithContentProviderFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            R.id.action_menu_maps -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, MapsFragment())
                    .addToBackStack("")
                    .commit()
            }
            R.id.action_menu_maps_yandex -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, MapsFragmentYandex())
                    .addToBackStack("")
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}




