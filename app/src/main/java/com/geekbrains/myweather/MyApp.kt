package com.geekbrains.myweather

import android.app.Application
import androidx.room.Room
import com.geekbrains.myweather.domain.room.HistoryDao
import com.geekbrains.myweather.domain.room.MyDB
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
    companion object{
        private var db: MyDB?=null
        private var appContext: MyApp?=null
        @OptIn(InternalCoroutinesApi::class)
        fun getHistoryDao(): HistoryDao{
            synchronized(MyDB::class.java){
                if (db==null){
                    if (appContext!=null){
                        db = Room.databaseBuilder(appContext!!, MyDB::class.java, "test")
                            .allowMainThreadQueries()
                            .build()
                    }else{
                        throw IllegalStateException("Что-то пошло не так, и у нас пустое appContext")
                    }
                }
            }
            return db!!.historyDao()
        }
    }
}