package com.example.alamiya_task.data.source.Database

import androidx.room.TypeConverter
import com.example.alamiya_task.data.model.Data
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class converters {

    private val gson = Gson()

    @TypeConverter
    fun fromDataList(dataList: List<Data>): String {
        return gson.toJson(dataList)
    }

    @TypeConverter
    fun toDataList(dataListString: String): List<Data> {
        val type = object : TypeToken<List<Data>>() {}.type
        return gson.fromJson(dataListString, type)
    }
}