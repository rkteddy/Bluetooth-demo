package me.rkteddy.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import java.util.*
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Created by Teddy on 2018/1/21 0018.
 */

class SearchActivity: BaseActivity() {

    private val MY_UUID = UUID.fromString("f710e010-b190-4d24-a47b-eb7b100bab39")
    private lateinit var mBluetoothLauncher: BluetoothLauncher
    val TAG = "SearchActivity"
    private val PERMISSION_REQUEST_COARSE_LOCATION = 1
    lateinit var mAdapter: DeviceListAdapter
    lateinit var mData: MutableList<BluetoothDevice>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        checkAccredit()
        initReceiver()
        initRecycleView()

        mBluetoothLauncher = BluetoothLauncher.get()
        mBluetoothLauncher.searchDevice()
    }

    /**
     * Initialze RecycleView
     */
    fun initRecycleView() {
        deviceList.layoutManager = LinearLayoutManager(this)
        mData = mutableListOf()
        mAdapter = DeviceListAdapter(mData)
        mAdapter.setOnItemClickListener(object: DeviceListAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                var device = mData[position]
                toast(device.name)
                var bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
                var t = Thread {
                    try {
                        bluetoothSocket.connect()
                    } catch (e: Exception) {
                        Log.e(TAG, "Bluetooth connection to server exception: ${e.message}")
                    }
                }
                t.start()
            }
        })
        deviceList.adapter = mAdapter
    }

    /**
     * Process for bluetooth successfully connect to server
     */
    fun manageConnectedSocket(socket: BluetoothSocket) {
        Log.e(TAG, "Successfully connect to server")
    }

    /**
     * Permission request
     */
    private fun checkAccredit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_COARSE_LOCATION)
            }
        }
    }

    /**
     * Initialize Receiver
     */
    fun initReceiver() {
        var filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(mBluetoothReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBluetoothReceiver)
    }

    private val mBluetoothReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var action = intent.action
            Log.e(TAG, "Action: $action")
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val scanDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    if (scanDevice == null || scanDevice!!.name == null) return
                    Log.e(TAG, "name=" + scanDevice!!.name + "address=" + scanDevice!!.address)
                    var name = scanDevice!!.name
                    var address = scanDevice!!.address
                    // Duplicate removal
                    var flag = true
                    mData.forEach { it ->
                        if (it.address == address)
                            flag = false
                    }
                    if (flag) {
                        mData.add(scanDevice)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.e(TAG, "Start scanning")
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.e(TAG, "Finish scanning")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "User permit location permission")
                mBluetoothLauncher.searchDevice()
            } else {
                Log.e(TAG, "User refuse location permission")
                mBluetoothLauncher.cancelSearch()
                toast("Please allow the application to use location permission")
                finish()
            }
        }
    }
}