package com.example.alamiya_task.presentation.qiblaDirection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.alamiya_task.common.util.Resource
import com.example.alamiya_task.data.model.PrayerTimeResponse
import com.example.alamiya_task.data.model.qibla.qiblaResponse
import com.example.alamiya_task.domin.use_case.PrayerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QiblaViewModel @Inject constructor(
    private val prayerUseCase: PrayerUseCase,
    private val app: Application,
) : AndroidViewModel(app) {

    //livedata objects
    private val _qiblaDirection = MutableLiveData<Resource<qiblaResponse>>()
    val qiblaDirection: LiveData<Resource<qiblaResponse>> = _qiblaDirection

    private val _direction = MutableLiveData<Double>()
    val direction: LiveData<Double> = _direction

    fun setDirection(d: Double) {
        _direction.postValue(d)
    }


    fun getDirection(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                val response = prayerUseCase.getQiblaDirection(lat, long)
                _qiblaDirection.postValue(getDirectionHandler(response))
            } catch (t: Throwable) {
                _qiblaDirection.postValue(Resource.Error(t.message, null))
            }

        }

    }

    private fun getDirectionHandler(response: Response<qiblaResponse>): Resource<qiblaResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }

}