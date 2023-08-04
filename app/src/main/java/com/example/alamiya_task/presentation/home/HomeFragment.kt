package com.example.alamiya_task.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.alamiya_task.R
import com.example.alamiya_task.common.util.Constants.Companion.COUNTDOWN_TIME_KEY
import com.example.alamiya_task.common.util.Resource
import com.example.alamiya_task.common.util.formatTimeTo12Hour
import com.example.alamiya_task.data.model.PrayerTimeResponse
import com.example.alamiya_task.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by activityViewModels()
    lateinit var binding: FragmentHomeBinding
    private val currentDate = LocalDate.now()
    private var currentDay = currentDate.dayOfMonth
    private var currentMonth = currentDate.month
    private var currentYear = currentDate.year
    private lateinit var prayerTimeList: List<String>
    private lateinit var locationHelper: LocationHelper
    private var firstTime = 0
    private var index = 0
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var prayersTimes: PrayerTimeResponse


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationHelper = LocationHelper(requireActivity() as AppCompatActivity)
        getUserLocation()
        calendarHandler()
        initObservation()

        binding.QiblaBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_qiblaFragment)
        }


    }


    @SuppressLint("SetTextI18n")
    private fun initObservation() {

        with(viewModel) {
            getPrayerTimeState.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let {

                            val prayTime = it.data[currentDay - 1].timings
                            prayerTimeList = listOf(
                                prayTime.Fajr.formatTimeTo12Hour(),
                                prayTime.Sunrise.formatTimeTo12Hour(),
                                prayTime.Dhuhr.formatTimeTo12Hour(),
                                prayTime.Asr.formatTimeTo12Hour(),
                                prayTime.Maghrib.formatTimeTo12Hour(),
                                prayTime.Isha.formatTimeTo12Hour()
                            )
                            viewModel.setPrayerTimes(prayerTimeList)
                            updateUI(it)
                            viewModel.deleteAll()
                            viewModel.saveAllPrayersTimes(it)

                        }
                    }

                    is Resource.Error -> {
                        updateUI(prayersTimes)
                    }

                    is Resource.Loading -> {}

                }
            }
            viewModel.getAllPrayersTimes().observe(viewLifecycleOwner) { response ->
                if (response != null)
                    prayersTimes = response
            }

            viewModel.prayerTimes.observe(viewLifecycleOwner) {
                if (firstTime == 0) {
                    firstTime++
                    //get next prayer time
                    val time = nextPrayer(it)
                    saveCountdownEndTime(requireContext() , time)

                    startCountdown(time / 1000)
                }

            }

            viewModel.lat.observe(viewLifecycleOwner) { lat ->
                viewModel.long.observe(viewLifecycleOwner) { long ->
                    viewModel.getPrayerTimes(currentYear, currentMonth.value, lat, long, 1)
                    latitude = lat
                    longitude = long

                }

            }

            // to handle count down
            viewModel.index.observe(viewLifecycleOwner) { index ->

                when (index) {
                    1 -> {
                        binding.tvNextpray.text = "Fajr"
                    }

                    2 -> {
                        binding.tvNextpray.text = "Sunrise"
                    }

                    3 -> {
                        binding.tvNextpray.text = "Duhr"
                    }

                    4 -> {
                        binding.tvNextpray.text = "Asr"
                    }

                    5 -> {
                        binding.tvNextpray.text = "Maghrib"
                    }

                    6 -> {
                        binding.tvNextpray.text = "Isha"
                    }
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    fun calendarHandler() {
        binding.tvDate.text = "$currentMonth $currentDay $currentYear"
        binding.btnIncrement.setOnClickListener {
            viewModel.getPrayerTimes(currentYear, currentMonth.value, latitude, longitude, 1)
            incrementLogic()
        }
        binding.decrementBtn.setOnClickListener {
            viewModel.getPrayerTimes(currentYear, currentMonth.value, latitude, longitude, 1)
            decrementLogic()

        }
    }

    internal fun saveCountdownEndTime(context: Context, endTimeMillis: Long) {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().putLong(COUNTDOWN_TIME_KEY, endTimeMillis).apply()
    }

    private var job: Job? = null


    @SuppressLint("SetTextI18n")
    fun startCountdown(totalSeconds: Long) {
        job = CoroutineScope(Dispatchers.Main).launch {
            for (seconds in totalSeconds downTo 0) {
                val formattedTime = formatTime(seconds)
                binding.tvCountdown.text = "          Time Left \n $formattedTime"
                delay(1000)
            }
            binding.tvCountdown.text = "Countdown finished for today will start at 12 am"
        }
    }

    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secondsRemaining = seconds % 60
        return String.format("%02d hr %02d min %02d sec", hours, minutes, secondsRemaining)
    }

    private fun convertTimeToMilliseconds(timeString: String): Long {
        return try {
            val format = SimpleDateFormat("hh:mm a", Locale.US)
            val date = format.parse(timeString)
            date?.time ?: -1
        } catch (e: Exception) {
            -1
        }
    }


    private fun nextPrayer(times: List<String>): Long {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = dateFormat.format(currentTime)
        val newTime = convertTimeToMilliseconds(formattedTime)
        for (i in times) {
            val x = convertTimeToMilliseconds(i)

            index++

            if (newTime < x) {
                val result = x - newTime
                viewModel.setIndex(index)
                return result
            }
        }
        return 0
    }

    private fun updateUI(data: PrayerTimeResponse) {
        with(binding) {
            val prayTime = data.data[currentDay - 1].timings
            tvFajr.text = prayTime.Fajr.formatTimeTo12Hour()
            tvSunrise.text = prayTime.Sunrise.formatTimeTo12Hour()
            tvDuhr.text = prayTime.Dhuhr.formatTimeTo12Hour()
            tvAsr.text = prayTime.Asr.formatTimeTo12Hour()
            tvMaghrib.text = prayTime.Maghrib.formatTimeTo12Hour()
            tvIsha.text = prayTime.Isha.formatTimeTo12Hour()
        }

    }

    private fun getUserLocation() {
        locationHelper.fetchLocation(object : LocationHelper.OnLocationFetchedListener {
            override fun onLocationFetched(location: Location?, address: String?) {
                viewModel.setLat(location!!.latitude)
                viewModel.setLong(location.longitude)
                binding.tvLocation.text = address

            }

            @SuppressLint("SetTextI18n")
            override fun onError(error: String?) {
                binding.tvLocation.text = "$error please restart the app"
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun incrementLogic() {
        if (currentDay < currentMonth.maxLength()) {
            currentDay += 1
            binding.tvDate.text = "$currentMonth  $currentDay $currentYear"
            viewModel.setDay(currentDay)
        } else {

            if (currentMonth == Month.DECEMBER && currentDay == currentMonth.maxLength()) {
                currentYear += 1
                viewModel.setYear(currentYear)
            }

            currentMonth += 1
            viewModel.setMonth(currentMonth.value)
            currentDay = 1
            viewModel.setDay(currentDay)
            binding.tvDate.text = "$currentMonth  $currentDay $currentYear"

        }
    }



    @SuppressLint("SetTextI18n")
    private fun decrementLogic() {
        if (currentDay > 1) {
            currentDay -= 1
            binding.tvDate.text = "$currentMonth $currentDay $currentYear"
            viewModel.setDay(currentDay)

        } else {

            if (currentMonth == Month.JANUARY && currentDay == 1) {
                currentYear -= 1
                viewModel.setYear(currentYear)
            }
            currentMonth -= 1
            viewModel.setMonth(currentMonth.value)
            currentDay = currentMonth.maxLength()
            viewModel.setDay(currentDay)
            binding.tvDate.text = "$currentMonth  $currentDay $currentYear"

        }
    }


}