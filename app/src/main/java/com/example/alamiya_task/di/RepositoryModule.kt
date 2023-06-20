package com.example.alamiya_task.di

import android.app.Application
import com.example.alamiya_task.data.Repository.RepositoryImp
import com.example.alamiya_task.data.source.Database.PrayerDatabase
import com.example.alamiya_task.data.source.RemoteData.ApiInterface
import com.example.alamiya_task.domin.repository.repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(api: ApiInterface, db: PrayerDatabase, app: Application): repository {
        return RepositoryImp(api, db)
    }


}