package com.geekbrains.myweather

data class Note(val title: String = "Заметка", val description: String = "Текст заметки")

object NotesBase{
    fun getTest(): String{
        if (0==0){
            return "test"
        }else{
            return "мир сошел с ума!"
        }
    }
}