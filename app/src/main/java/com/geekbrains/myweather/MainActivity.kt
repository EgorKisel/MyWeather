package com.geekbrains.myweather

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.press_me)
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, First::class.java)
            intent.putExtra("key", "Kotlin")
            startActivity(intent)
        }
    }

}



