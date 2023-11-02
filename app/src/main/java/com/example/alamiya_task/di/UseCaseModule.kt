package com.example.alamiya_task.di

import com.example.alamiya_task.data.Repository.RepositoryImp
import com.example.alamiya_task.domin.repository.repository
import com.example.alamiya_task.domin.use_case.DeleteTableUseCase
import com.example.alamiya_task.domin.use_case.GetAllPrayersTimesUseCase
import com.example.alamiya_task.domin.use_case.GetPrayersTimesUseCase
import com.example.alamiya_task.domin.use_case.GetQiblaDirectionUseCase
import com.example.alamiya_task.domin.use_case.PrayerUseCases
import com.example.alamiya_task.domin.use_case.SavePrayersTimesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {


    @Provides
    @Singleton
    fun provideUseCase(repository: repository): PrayerUseCases {
        return PrayerUseCases(
            DeleteTableUseCase(repository),
            GetAllPrayersTimesUseCase(repository),
            GetPrayersTimesUseCase(repository),
            GetQiblaDirectionUseCase(repository),
            SavePrayersTimesUseCase(repository),
        )
    }

}