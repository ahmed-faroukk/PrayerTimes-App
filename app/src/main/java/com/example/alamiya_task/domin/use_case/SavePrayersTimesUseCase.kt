package com.example.alamiya_task.domin.use_case

import com.example.alamiya_task.data.Repository.RepositoryImp
import com.example.alamiya_task.data.model.PrayerTimeResponse
import com.example.alamiya_task.domin.repository.repository
import javax.inject.Inject

class SavePrayersTimesUseCase  @Inject constructor(
    private val repository: repository,
) {

    suspend operator fun invoke(
        response: PrayerTimeResponse,
    ): Long {
        return repository.savePrayersTimes(response)
    }
}