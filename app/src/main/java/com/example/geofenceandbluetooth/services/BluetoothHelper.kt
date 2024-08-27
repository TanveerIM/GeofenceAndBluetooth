package com.example.geofenceandbluetooth.services

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class BluetoothHelper(private val context: Context, private val map: GoogleMap) {
    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
    private var bluetoothMarker: Marker? = null

    @SuppressLint("MissingPermission")
    fun connectToDevice(deviceAddress: String) {
        val device: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(deviceAddress)
        device?.let {
            // Simulate getting the device location (Replace this with actual location logic)
            val deviceLocation = LatLng(37.7749, -122.4194) // Example location
            if (bluetoothMarker == null) {
                bluetoothMarker = map.addMarker(MarkerOptions().position(deviceLocation).title("Bluetooth Device"))
            } else {
                bluetoothMarker?.position = deviceLocation
            }
            Log.d("BluetoothHelper", "Connected to ${device.name}")
        } ?: run {
            Log.e("BluetoothHelper", "Device not found")
        }
    }

    fun updateDeviceLocation(latLng: LatLng) {
        if (bluetoothMarker == null) {
            bluetoothMarker = map.addMarker(MarkerOptions().position(latLng).title("Bluetooth Device"))
        } else {
            bluetoothMarker?.position = latLng
        }
    }

    @SuppressLint("MissingPermission")
    fun disconnectFromDevice(deviceAddress: String) {
        val device: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(deviceAddress)
        device?.let {
            // Initiate disconnection process
            Log.d("BluetoothHelper", "Disconnected from ${device.name}")
        } ?: run {
            Log.e("BluetoothHelper", "Device not found")
        }
    }
}