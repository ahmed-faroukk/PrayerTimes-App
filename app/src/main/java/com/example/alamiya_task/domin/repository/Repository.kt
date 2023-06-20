package com.example.alamiya_task.domin.repository

import androidx.lifecycle.LiveData
import com.example.alamiya_task.data.model.PrayerTimeResponse
import com.example.alamiya_task.data.model.qibla.qiblaResponse
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface repository {

    suspend fun getPrayerTimes(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
        method: Int,
    ): Response<PrayerTimeResponse>

    suspend fun getQiblaDirection(
        latitude: Double,
        longitude: Double,
    ): Response<qiblaResponse>

    suspend fun savePrayersTimes(response: PrayerTimeResponse): Long

    fun getAllPrayersTimes(): LiveData<PrayerTimeResponse>

    suspend fun deleteAll()
}