package com.example.alamiya_task.di

import android.content.Context
import androidx.room.Room
import com.example.alamiya_task.data.source.Database.PrayerDao
import com.example.alamiya_task.data.source.Database.PrayerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providePrayerDatabase(@ApplicationContext context: Context): PrayerDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PrayerDatabase::class.java,
            "article_db.db"
        ).build()
    }

    @Provides
    fun providePrayerDao(database: PrayerDatabase): PrayerDao {
        return database.getPrayerDao()
    }
}
