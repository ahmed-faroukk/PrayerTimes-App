package com.example.alamiya_task.domin.use_case

data class PrayerUseCases(
    val deleteTableUseCase: DeleteTableUseCase ,
    val getAllPrayersTimesUseCase: GetAllPrayersTimesUseCase ,
    val getPrayersTimesUseCase: GetPrayersTimesUseCase ,
    val getQiblaDirectionUseCase: GetQiblaDirectionUseCase ,
    val savePrayersTimesUseCase: SavePrayersTimesUseCase ,
)
