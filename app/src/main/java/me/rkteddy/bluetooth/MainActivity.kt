package me.rkteddy.bluetooth

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val REQUEST_ENABLE_BT = 1
    private lateinit var mBluetoothLauncher: BluetoothLauncher
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            mBluetoothLauncher = BluetoothLauncher.get()
            mBluetoothLauncher.launchBluetooth(this, REQUEST_ENABLE_BT)
            mBluetoothLauncher.startServerSocket()
        } catch (e: NullPointerException) {
            Log.e(TAG, e.message)
        } catch (e: RuntimeException) {
            Log.e(TAG, e.message)
        }
    }

    /**
     * Show short toast
     */
    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * Launch bluetooth
     */
    fun launch(v: View) {
        mBluetoothLauncher.launchBluetooth(this, REQUEST_ENABLE_BT)
    }

    /**
     * Open search activity
     */
    fun search(v: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e(TAG, "Bluetooth launched, start server")
                mBluetoothLauncher.startServerSocket()
            } else {
                Log.e(TAG, "User refuse to launch bluetooth")
            }
        }
    }
}
