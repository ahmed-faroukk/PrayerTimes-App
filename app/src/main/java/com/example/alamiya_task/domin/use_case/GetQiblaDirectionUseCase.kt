package com.example.alamiya_task.domin.use_case

import com.example.alamiya_task.data.Repository.RepositoryImp
import com.example.alamiya_task.data.model.qibla.qiblaResponse
import com.example.alamiya_task.domin.repository.repository
import kotlinx.coroutines.CoroutineScope
import retrofit2.Response
import javax.inject.Inject

class GetQiblaDirectionUseCase @Inject constructor(
    private val repository: repository,
) {
     suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
    ): Response<qiblaResponse> {
        return repository.getQiblaDirection(latitude, longitude)
    }
}