package com.jflavio1.androidmqttexample.presenters

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Build
import com.jflavio1.androidmqttexample.mqtt.SensorsMqttService
import com.jflavio1.androidmqttexample.views.SensorsListView
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.jflavio1.androidmqttexample.model.TempSensor
import com.jflavio1.androidmqttexample.viewmodel.TempSensorViewModel


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
        val vm = ViewModelProviders.of(this.view.getViewContext() as FragmentActivity).get(TempSensorViewModel::class.java)
        vm.getSensors().observe(this.view.getViewContext() as FragmentActivity, Observer<ArrayList<TempSensor>> {
            this.view.setSensorsTemperature(it!!.toList() as ArrayList<TempSensor>)
        })
    }

}