package com.example.geofenceandbluetooth.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.geofenceandbluetooth.broadcastreceiver.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import kotlin.math.sqrt

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val app = application
    private var geofencingClient = LocationServices.getGeofencingClient(application.applicationContext)

    var geoId: Long = 0L
    var geoRadius: Float = 50f

    @SuppressLint("MissingPermission")
    fun startGeofence(latitude: Double, longitude: Double) {
        val geofence = Geofence.Builder()
            .setRequestId(geoId.toString())
            .setCircularRegion(
                latitude,
                longitude,
                geoRadius
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER
                        or Geofence.GEOFENCE_TRANSITION_EXIT
                        or Geofence.GEOFENCE_TRANSITION_DWELL
            )
            .setLoiteringDelay(2000)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(
                GeofencingRequest.INITIAL_TRIGGER_ENTER
                        or GeofencingRequest.INITIAL_TRIGGER_EXIT
                        or GeofencingRequest.INITIAL_TRIGGER_DWELL
            )
            .addGeofence(geofence)
            .build()

        geofencingClient.addGeofences(geofencingRequest, setPendingIntent(geoId.toInt())).run {
            addOnSuccessListener {
                Log.d("Geofence", "Successfully added.")
            }
            addOnFailureListener {
                Log.e("Geofence", "Error: ${it.message.toString()}")
            }
        }
    }

    private fun setPendingIntent(geoId: Int): PendingIntent {
        val intent = Intent(app, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            app,
            geoId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun getBounds(center: LatLng, radius: Float): LatLngBounds {
        val distanceFromCenterToCorner = radius * sqrt(2.0)
        val southWestCorner = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0)
        val northEastCorner = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0)
        return LatLngBounds(southWestCorner, northEastCorner)
    }
}