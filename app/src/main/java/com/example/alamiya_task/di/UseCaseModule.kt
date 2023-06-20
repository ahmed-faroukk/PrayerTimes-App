package com.example.alamiya_task.di

import com.example.alamiya_task.data.Repository.RepositoryImp
import com.example.alamiya_task.domin.use_case.PrayerUseCase
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
    fun provideUseCase(repositoryImp: RepositoryImp): PrayerUseCase {
        return PrayerUseCase(repositoryImp)
    }

}