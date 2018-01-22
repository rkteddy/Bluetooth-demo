package me.rkteddy.bluetooth

import android.bluetooth.BluetoothDevice
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class DeviceListAdapter(data: MutableList<BluetoothDevice>) : RecyclerView.Adapter<DeviceListAdapter.MyViewHolder>() {

    private var mData: MutableList<BluetoothDevice> = data
    private var mOnItemClickListener: OnItemClickListener? = null

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
        holder!!.setItem(mData[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DeviceListAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val view = inflater.inflate(R.layout.list_device, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var mName: TextView = itemView.findViewById(R.id.name)
        private var mAddress: TextView = itemView.findViewById(R.id.address)

        fun setItem(device: BluetoothDevice) {
            mName.text = device.name
            mAddress.text = device.address
        }
    }
}