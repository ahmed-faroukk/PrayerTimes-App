package com.example.alamiya_task.domin.use_case

import androidx.lifecycle.LiveData
import com.example.alamiya_task.data.Repository.RepositoryImp
import com.example.alamiya_task.data.model.PrayerTimeResponse
import com.example.alamiya_task.data.model.qibla.qiblaResponse
import retrofit2.Response
import javax.inject.Inject

class PrayerUseCase @Inject constructor(
    private val repositoryImp: RepositoryImp,
) {

    suspend fun getPrayersTimes(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
        method: Int,
    ): Response<PrayerTimeResponse> {
        return repositoryImp.getPrayerTimes(year, month, latitude, longitude, method)
    }

    suspend fun getQiblaDirection(
        latitude: Double,
        longitude: Double,
    ): Response<qiblaResponse> {
        return repositoryImp.getQiblaDirection(latitude, longitude)
    }

    suspend fun savePrayersTimes(
        response: PrayerTimeResponse,
    ): Long {
        return repositoryImp.savePrayersTimes(response)
    }

    fun getAllPrayersTimes(
    ): LiveData<PrayerTimeResponse> {
        return repositoryImp.getAllPrayersTimes()
    }

    suspend fun deleteTable() {
        repositoryImp.deleteAll()

    }


}