package com.jflavio1.androidmqttexample.presenters

import android.content.Intent
import android.os.Build
import com.jflavio1.androidmqttexample.mqtt.SensorsMqttService
import com.jflavio1.androidmqttexample.views.SensorsListView


/**
 * SensorsListPresenterImpl
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
class SensorsListPresenterImpl(val view: SensorsListView) : SensorsListPresenter {

    private val TAG = "SensorsPresenter"

    init {
        this.view.setSensorPresenter(this)
    }

    override fun initMqttService() {

        val startServiceIntent = Intent(this.view.getViewContext(), SensorsMqttService::class.java)
        startServiceIntent.action = SensorsMqttService.MQTT_CONNECT

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.view.getViewContext().startForegroundService(startServiceIntent)
        } else {
            this.view.getViewContext().startService(startServiceIntent)
        }

    }

    override fun getTemperatures() {
        // TODO call MQTT
    }

}