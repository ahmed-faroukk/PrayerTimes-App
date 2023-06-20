package com.example.alamiya_task.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "prayers"
)
data class PrayerTimeResponse(
    @PrimaryKey(
        autoGenerate = true
    )
    var id : Int? = null,
    val code: Int,
    val data : List<Data>,
    val status: String
)