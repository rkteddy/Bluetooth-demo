package me.rkteddy.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import java.util.*

/**
 * Created by Teddy on 2018/1/21 0021.
 */

class BluetoothLauncher {
    private val TAG = "BluetoothLauncher"
    private val mBluetoothAdapter: BluetoothAdapter
    private var mmServerSocket: BluetoothServerSocket? = null
    private val MY_UUID = UUID.fromString("f710e010-b190-4d24-a47b-eb7b100bab39")

    private constructor(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                ?: throw NullPointerException("Can't launch bluetooth")
    }

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
            val enabler = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            context.startActivityForResult(enabler, requestCode)
        }
    }

    /**
     * Start server
     */
    fun startServerSocket() {
        if (!mBluetoothAdapter.isEnabled) {
            throw RuntimeException("Please launch bluetooth")
        }
        if (mmServerSocket == null) {
            return
        }
        mmServerSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Teddy", MY_UUID)
        // New thread to start a server socket and wait for connection
        var thread = Thread {
            var socket = mmServerSocket!!.accept()
            if (socket == null) {
                return@Thread
            }
            manageConnectedSocket(socket)
        }
        thread.start()
    }

    /**
     * Connected socket manage
     */
    fun manageConnectedSocket(socket: BluetoothSocket) {
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