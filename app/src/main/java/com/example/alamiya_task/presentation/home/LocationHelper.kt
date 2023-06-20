package com.example.alamiya_task.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

@Suppress("DEPRECATION")
class LocationHelper(private val context: AppCompatActivity) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var location: Location? = null

    interface OnLocationFetchedListener {
        fun onLocationFetched(location: Location?, address: String?)
        fun onError(error: String?)
    }

    fun fetchLocation(onLocationFetchedListener: OnLocationFetchedListener) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location == null) {
                    onLocationFetchedListener.onError("Unable to fetch location please enable permissions")
                    return@addOnSuccessListener
                }
                this.location = location
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )!!
                val address: Address = addresses[0]
                val addressText = address.getAddressLine(0)
                onLocationFetchedListener.onLocationFetched(location, addressText)
            }
        } else {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }
}