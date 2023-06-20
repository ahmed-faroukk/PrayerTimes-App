package com.example.alamiya_task.data.source.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.alamiya_task.data.model.PrayerTimeResponse

@Database(
    entities = [PrayerTimeResponse::class],
    version = 1
)
@TypeConverters(converters::class)
abstract class PrayerDatabase : RoomDatabase() {
    // will implemented by room behind the sense because it abstract
    abstract fun getPrayerDao(): PrayerDao

    companion object {
        // i use Volatile because it allow other threads to immediately see when the thread changes this instance
        @Volatile
        private var instance: PrayerDatabase? = null

        // to make sure that only single instance for our database
        private var Lock = Any()

        // if no instance will synchronized
        operator fun invoke(context: Context) = instance ?: synchronized(Lock) {
            // if no instance will create one

            instance ?: createDatabase(context).also { instance = it }

        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PrayerDatabase::class.java,
                "article_db.db"


            ).build()


    }

}