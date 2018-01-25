package me.rkteddy.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import java.io.IOException
import java.util.*

class BluetoothLauncher private constructor() {
    private val TAG = "BluetoothLauncher"
    private val MY_UUID = UUID.fromString("f710e010-b190-4d24-a47b-eb7b100bab39")
    private val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            ?: throw NullPointerException("Device does not support Bluetooth")
    private var mmServerSocket: BluetoothServerSocket? = null

    companion object {
        fun get(): BluetoothLauncher{
            return Inner.singleInstance
        }
    }

    private object Inner {
        val singleInstance = BluetoothLauncher()
    }

    /**
     * Launch bluetooth
     */
    fun launchBluetooth(context: Activity, requestCode: Int) {
        if (!mBluetoothAdapter.isEnabled) {
            val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            context.startActivityForResult(enableBTIntent, requestCode)
        }
    }

    /**
     * Start server
     */
    fun startServerSocket() {
        if (!mBluetoothAdapter.isEnabled) {
            throw RuntimeException("Please launch bluetooth")
        }
        mmServerSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Teddy", MY_UUID)
        mmServerSocket?: return
        // New thread to start a server socket and wait for connection
        val serverThread = Thread {
            var socket: BluetoothSocket? = null
            while(true){
                try{
                     socket = mmServerSocket!!.accept()
                } catch (e: IOException) {
                    break
                }
                if (socket != null) {
                    manageConnectedSocket(socket)
                    mmServerSocket!!.close()
                }
            }
        }
        serverThread.start()
    }

    /**
     * Connected socket manage
     */
    private fun manageConnectedSocket(socket: BluetoothSocket) {
        Log.e(TAG, "Device successfully connected")
    }

    /**
     * Search devices
     */
    fun searchDevice() {
        if (mBluetoothAdapter.isDiscovering) {
            mBluetoothAdapter.cancelDiscovery()
        }
        mBluetoothAdapter.startDiscovery()
    }

    /**
     * Stop Searching
     */
    fun cancelSearch() {
        if (mBluetoothAdapter.isDiscovering) {
            mBluetoothAdapter.cancelDiscovery()
        }
    }
}