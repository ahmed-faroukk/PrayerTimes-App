package com.example.alamiya_task.domin.use_case

import com.example.alamiya_task.data.Repository.RepositoryImp
import com.example.alamiya_task.data.model.PrayerTimeResponse
import com.example.alamiya_task.domin.repository.repository
import retrofit2.Response
import javax.inject.Inject

class GetPrayersTimesUseCase @Inject constructor(
    private val repository: repository,
) {

    suspend operator fun invoke(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
        method: Int,
    ): Response<PrayerTimeResponse> {
        return repository.getPrayerTimes(year, month, latitude, longitude, method)
    }


}