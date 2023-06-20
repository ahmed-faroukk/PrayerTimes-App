package com.example.alamiya_task.data.source.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alamiya_task.data.model.PrayerTimeResponse

@Dao
interface PrayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePrayersTimes(prayerTimeResponse: PrayerTimeResponse): Long

    @Query("SELECT * FROM prayers")
    fun getAllPrayersTimes(): LiveData<PrayerTimeResponse>


    @Query("delete FROM prayers")
    suspend fun deleteAll()

}