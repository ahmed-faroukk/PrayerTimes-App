package com.example.alamiya_task.presentation.qiblaDirection

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.example.alamiya_task.R
import com.example.alamiya_task.common.util.Resource
import com.example.alamiya_task.databinding.FragmentQiblaBinding
import com.example.alamiya_task.presentation.home.LocationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import kotlin.math.atan2


@Suppress("DEPRECATION")
@AndroidEntryPoint
class QiblaFragment : Fragment(R.layout.fragment_qibla), SensorEventListener {
    private lateinit var binding: FragmentQiblaBinding
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map: MapView
    private lateinit var locationHelper: LocationHelper
    lateinit var mapController: IMapController
    private val viewModel: QiblaViewModel by activityViewModels()
    private var qiblaDirection: Double = 0.0

    // sensor
    private lateinit var sensorManager: SensorManager
    private lateinit var gyroscope: Sensor
    val lat = 0.0
    val long = 0.0
    var qabaDirection = 0.0

    // polygon
    private val line = Polyline()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentQiblaBinding.inflate(inflater, container, false)
        map = binding.map
        //inflate and create the map
        map.setTileSource(TileSourceFactory.MAPNIK)
        mapController = map.controller
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)



        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        locationHelper = LocationHelper(requireActivity() as AppCompatActivity)
        mapController.setZoom(3.5)
        detectLocations()
        setMapCompass()
        initObservation()

        Log.d("qiblaDireaction" , qiblaDirection.toString())


    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
        sensorManager.unregisterListener(this)
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun createMarkerOnTheMap(resourceId: Int, lat: Double, long: Double) {
        Marker(map)
        val m = Marker(map)
        m.position = GeoPoint(lat, long)
        m.icon = resources.getDrawable(resourceId)
        m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP)
        map.overlays
            .add(m)
    }

    private fun setMapCompass() {
        // map compass
        val compassOverlay =
            CompassOverlay(context, InternalCompassOrientationProvider(context), map)
        compassOverlay.enableCompass()
        map.overlays.add(compassOverlay)
    }

    fun animateZoom(startZoom: Double, endZoom: Double, durationMillis: Long) {
        val zoomDifference = kotlin.math.abs(startZoom - endZoom)
        val zoomPerMillis = zoomDifference / durationMillis

        CoroutineScope(Dispatchers.Main).launch {
            val startTime = System.currentTimeMillis()
            var currentZoom = startZoom
            while (currentZoom < endZoom) {
                val elapsedMillis = System.currentTimeMillis() - startTime
                currentZoom = startZoom + (zoomPerMillis * elapsedMillis)
                mapController.setZoom(currentZoom)
                delay(16) // Delay for smooth animation (approximately 60 FPS)
            }
            mapController.setZoom(endZoom) // Ensure final zoom is set accurately
        }
    }

    private fun detectLocations() {
        // kabaa location 21.4225° N, 39.8262° E
        binding.locationbtn.setOnClickListener {
            locationHelper.fetchLocation(object : LocationHelper.OnLocationFetchedListener {
                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onLocationFetched(location: Location?, address: String?) {


                    // animation will zoom in map
                    animateZoom(3.5, 6.5, 1000)
                    if (location != null) {
                        // draw poly line
                        val startPoint = GeoPoint(location.latitude, location.longitude)
                        val endPoint = GeoPoint(21.4225, 39.8262)
                        mapController.setCenter(startPoint)
                        drawPolyLine(startPoint, endPoint)
                        // place markers
                        createMarkerOnTheMap(
                            R.drawable.baseline_location_on_24,
                            location.latitude,
                            location.longitude
                        )
                        createMarkerOnTheMap(R.drawable.baseline_mosque_24, 21.4225, 39.8262)

                        location.latitude = lat
                        location.longitude = long
                    }
                }

                override fun onError(error: String?) {
                    Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_LONG).show()
                }
            })


        }

    }

    fun drawPolyLine(startPoint: GeoPoint, endPoint: GeoPoint) {

        val geoPoints: MutableList<GeoPoint> = ArrayList()
        geoPoints.add(startPoint)
        geoPoints.add(endPoint)
        line.setPoints(geoPoints)
        line.setOnClickListener { polyline, mapView, _ ->
            Toast.makeText(
                mapView.context,
                "Polyline with " + polyline.points.size + " Qibla Direction",
                Toast.LENGTH_LONG
            ).show()
            false
        }
        map.overlayManager.add(line)

    }

    override fun onSensorChanged(event: SensorEvent) {
        // Get the gyroscope sensor readings for rotation
        val rotationX = event.values[0]
        val rotationY = event.values[1]
        //   val rotationZ = event.values[2]
        viewModel.setDirection(calculateDirection(rotationX, rotationY))


    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Handle accuracy changes if needed

    }


    private fun calculateDirection(x: Float, y: Float): Double {
        val azimuth = atan2(y.toDouble(), x.toDouble())
        return Math.toDegrees(azimuth)
    }


    private fun initObservation() {
        viewModel.qiblaDirection.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        val d = response.data.data.direction
                        qabaDirection = d

                    }
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Loading -> {}

            }

        }

        viewModel.direction.observe(viewLifecycleOwner) {
            Log.d("dideaction" , it.toString())
            if (qabaDirection==it){
                binding.view.setBackgroundColor(Color.GREEN)
            }else {
                binding.view.setBackgroundColor(Color.RED)
            }
        }
    }


}
