package me.rkteddy.bluetooth

import android.bluetooth.BluetoothDevice
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by Teddy on 2018/1/21 0021.
 */

class DeviceListAdapter: RecyclerView.Adapter<DeviceListAdapter.MyViewHolder> {

    private lateinit var mData: MutableList<BluetoothDevice>
    private var mOnItemClickListener: OnItemClickListener? = null

    constructor(data: MutableList<BluetoothDevice>) {
        mData = data
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        if (mOnItemClickListener != null) {
            holder!!.itemView.setOnClickListener { _ ->
                mOnItemClickListener!!.onItemClick(holder.itemView, holder.layoutPosition)
            }
        }
        holder!!.setItem(mData.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DeviceListAdapter.MyViewHolder {
        var inflater = LayoutInflater.from(parent!!.context)
        var view = inflater.inflate(R.layout.list_device, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder: RecyclerView.ViewHolder {
        private var mName: TextView
        private var mAddress: TextView

        constructor(itemView: View): super(itemView) {
            mName = itemView.findViewById(R.id.name)
            mAddress = itemView.findViewById(R.id.address)
        }

        fun setItem(device: BluetoothDevice) {
            mName.text = device.name
            mAddress.text = device.address
        }
    }
}