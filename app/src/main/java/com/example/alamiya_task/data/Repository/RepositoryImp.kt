package com.example.alamiya_task.data.Repository

import androidx.lifecycle.LiveData
import com.example.alamiya_task.data.model.PrayerTimeResponse
import com.example.alamiya_task.data.model.qibla.qiblaResponse
import com.example.alamiya_task.data.source.Database.PrayerDatabase
import com.example.alamiya_task.data.source.RemoteData.ApiInterface
import com.example.alamiya_task.domin.repository.repository
import retrofit2.Response

import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val apiInterface: ApiInterface,
    private val db: PrayerDatabase,
) : repository {


    override suspend fun getPrayerTimes(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
        method: Int,
    ): Response<PrayerTimeResponse> =
        apiInterface.getPrayerTimes(year, month, latitude, longitude, method)

    override suspend fun getQiblaDirection(
        latitude: Double,
        longitude: Double,
    ): Response<qiblaResponse> = apiInterface.getQiblaDirection(latitude, longitude)

    override suspend fun savePrayersTimes(response: PrayerTimeResponse): Long =
        db.getPrayerDao().savePrayersTimes(response)


    override fun getAllPrayersTimes(): LiveData<PrayerTimeResponse> =
        db.getPrayerDao().getAllPrayersTimes()

    override suspend fun deleteAll() =
        db.getPrayerDao().deleteAll()


}