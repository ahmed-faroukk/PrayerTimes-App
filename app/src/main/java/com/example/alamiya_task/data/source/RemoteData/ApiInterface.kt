package com.example.alamiya_task.data.source.RemoteData

import com.example.alamiya_task.data.model.PrayerTimeResponse
import com.example.alamiya_task.data.model.qibla.qiblaResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("calendar/{year}/{month}")
    suspend fun getPrayerTimes(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int,
    ): Response<PrayerTimeResponse>

    @GET("qibla/{latitude}/{longitude}")
    suspend fun getQiblaDirection(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
    ): Response<qiblaResponse>


}