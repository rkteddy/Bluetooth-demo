package me.rkteddy.bluetooth

import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * Created by Teddy on 2018/1/21 0018.
 */

open class BaseActivity: AppCompatActivity() {
    /**
     * 显示toast
     */
    fun toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, text, duration).show();
    }
}