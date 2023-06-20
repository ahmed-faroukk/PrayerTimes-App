package com.example.alamiya_task.data.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alamiya_task.common.util.Resource
import com.example.alamiya_task.data.model.PrayerTimeResponse
import com.example.alamiya_task.data.model.qibla.Data
import com.example.alamiya_task.data.model.qibla.qiblaResponse
import com.example.alamiya_task.domin.repository.repository
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Response

class FakeRepositoryImp : repository{

    private var response = PrayerTimeResponse(1,5, listOfNotNull(),"test")
    private val observablePrayerTimes = MutableLiveData<PrayerTimeResponse>(response)

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observablePrayerTimes.postValue(response)
    }




    override suspend fun getPrayerTimes(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
        method: Int,
    ): Response<PrayerTimeResponse> {
        if (shouldReturnNetworkError) {
            return Response.error(500, ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                "Network Error"
            ))
        }

        // Simulate a successful API response
        val prayerTimeResponse = PrayerTimeResponse(1, 5, listOfNotNull(), "test")
        return Response.success(prayerTimeResponse)

    }

    override suspend fun getQiblaDirection(
        latitude: Double,
        longitude: Double,
    ): Response<qiblaResponse> {
        if (shouldReturnNetworkError) {
            return Response.error(500, ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                "Network Error"
            ))
        }

        // Simulate a successful API response
        val qiblaResponse = qiblaResponse(1, data = Data(642.56 , 55.6846 , 56.56),"test")
        return Response.success(qiblaResponse)
    }


    override suspend fun savePrayersTimes(response: PrayerTimeResponse): Long {
        this.response = PrayerTimeResponse(1,5, listOfNotNull(),"test")
        refreshLiveData()
        return response.id!!.toLong()
    }

    override fun getAllPrayersTimes(): LiveData<PrayerTimeResponse> {
        return observablePrayerTimes
    }

    override suspend fun deleteAll() {
        response = PrayerTimeResponse(0,0, listOfNotNull(),"")
        refreshLiveData()
    }
}