package com.geekbrains.myweather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val textView = findViewById<TextView>(R.id.text_view)
        textView.text = list.toString()

        val button = findViewById<Button>(R.id.press_me)
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, First::class.java)
            intent.putExtra("key", "Kotlin")
            startActivity(intent)



            val noteOne = Note("Заметка 1", "Текст заметки 1")
            val noteSecond = noteOne.copy(title = "Заметка 2", description = "Текст заметки 2")
            fun noteToString(): String {
                val noteSecondToString: String = noteSecond.title + "" + noteSecond.description
                return noteSecondToString
            }
            Log.d("tag", noteSecond.title)
            Log.d("listOf", "$list")
        }
    }
    private val list = listOf("City", "Weather", "Year", "Day")
}




