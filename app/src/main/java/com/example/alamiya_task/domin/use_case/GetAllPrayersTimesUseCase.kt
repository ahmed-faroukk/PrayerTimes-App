package com.example.alamiya_task.domin.use_case

import androidx.lifecycle.LiveData
import com.example.alamiya_task.data.Repository.RepositoryImp
import com.example.alamiya_task.data.model.PrayerTimeResponse
import com.example.alamiya_task.domin.repository.repository
import javax.inject.Inject

class GetAllPrayersTimesUseCase  @Inject constructor(
    private val repository: repository,
) {

    operator fun invoke(
    ): LiveData<PrayerTimeResponse> {
        return repository.getAllPrayersTimes()
    }
}