package com.jflavio1.androidmqttexample.views

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jflavio1.androidmqttexample.R
import com.jflavio1.androidmqttexample.model.CustomLightSensor

/**
 * SensorsAdapter
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  13/5/17
 */
class SensorsAdapter(val listener: SensorsAdapterListener) : RecyclerView.Adapter<SensorsAdapter.Holder>() {

    private var sensorsList = arrayListOf<CustomLightSensor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_sensor, parent, false))
    }

    fun updateAllList(list: ArrayList<CustomLightSensor>) {
        this.sensorsList = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = sensorsList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.fillData(sensorsList[position])
        holder.itemView.findViewById<ImageView>(R.id.itemSensor_iv_light).setOnClickListener {
            listener.onSensorLightClick(sensorsList[position])
        }
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun fillData(sensor: CustomLightSensor) {
            itemView.findViewById<TextView>(R.id.itemSensor_tv_name).text = sensor.name

            if (sensor.lightOn) {
                itemView.findViewById<ImageView>(R.id.itemSensor_iv_light).setBackgroundResource(R.drawable.ic_light)
            } else {
                itemView.findViewById<ImageView>(R.id.itemSensor_iv_light).setBackgroundResource(R.drawable.ic_light_off)
            }

        }
    }

    interface SensorsAdapterListener {
        fun onSensorLightClick(sensor: CustomLightSensor)
    }

}