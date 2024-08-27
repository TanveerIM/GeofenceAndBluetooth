package com.example.geofenceandbluetooth.services

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log

class BluetoothHelper(private val context: Context) {
    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter

    @SuppressLint("MissingPermission")
    fun connectToDevice(deviceAddress: String) {
        val device: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(deviceAddress)
        device?.let {
            // Initiate connection process
            // For simplicity, assuming Bluetooth connection logic is implemented here
            Log.d("BluetoothHelper", "Connecting to ${device.name}")
        } ?: run {
            Log.e("BluetoothHelper", "Device not found")
        }
    }

    @SuppressLint("MissingPermission")
    fun disconnectFromDevice(deviceAddress: String) {
        val device: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(deviceAddress)
        device?.let {
            // Initiate disconnection process
            Log.d("BluetoothHelper", "Disconnecting from ${device.name}")
        } ?: run {
            Log.e("BluetoothHelper", "Device not found")
        }
    }
}