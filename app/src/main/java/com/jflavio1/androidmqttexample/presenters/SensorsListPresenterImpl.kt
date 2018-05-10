package com.jflavio1.androidmqttexample.presenters

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.*
import android.hardware.Sensor
import android.os.Build
import android.os.IBinder
import android.support.v4.app.FragmentActivity
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.jflavio1.androidmqttexample.model.TempSensor
import com.jflavio1.androidmqttexample.mqtt.SensorsMqttService
import com.jflavio1.androidmqttexample.repository.SensorsRepository
import com.jflavio1.androidmqttexample.viewmodel.TempSensorViewModel
import com.jflavio1.androidmqttexample.views.SensorsListView
import com.jflavio1.androidmqttexample.mqtt.SensorsMqttService.LocalBinder




/**
 * SensorsListPresenterImpl
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
class SensorsListPresenterImpl(val view: SensorsListView) : SensorsListPresenter {

    private lateinit var repository : SensorsRepository
    private var mqttService: SensorsMqttService? = null
    private var mqttBroadcast: MqttBroadcast

    init {
        mqttBroadcast = MqttBroadcast()
        this.view.setSensorPresenter(this)
    }

    inner class MqttBroadcast: BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            if(SensorsMqttService.CONNECTION_SUCCESS == intent!!.action){
                view.onMqttConnected()
            }

            if(SensorsMqttService.CONNECTION_FAILURE == intent.action){
                view.onMqttError("error on connecting")
            }

            if(SensorsMqttService.CONNECTION_LOST == intent.action){
                view.onMqttError("connection lost")
                view.onMqttDisconnected()
            }

            if(SensorsMqttService.DISCONNECT_SUCCESS == intent.action){
                view.onMqttStopped()
            }
        }

    }

    override fun initMqttService() {

        LocalBroadcastManager.getInstance(this.view.getViewContext()).registerReceiver(mqttBroadcast, IntentFilter(SensorsMqttService.CONNECTION_SUCCESS))

        val startServiceIntent = Intent(this.view.getViewContext(), SensorsMqttService::class.java)
        this.view.getViewContext().bindService(startServiceIntent, serviceConnection, 0)

        startServiceIntent.action = SensorsMqttService.MQTT_CONNECT

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.view.getViewContext().startForegroundService(startServiceIntent)
        } else {
            this.view.getViewContext().startService(startServiceIntent)
        }

    }

    override fun stopMqttService() {
        this.view.getViewContext().unbindService(serviceConnection)
        LocalBroadcastManager.getInstance(this.view.getViewContext()).unregisterReceiver(mqttBroadcast)
        val startServiceIntent = Intent(this.view.getViewContext(), SensorsMqttService::class.java)
        startServiceIntent.action = SensorsMqttService.MQTT_DISCONNECT
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
        this.repository.getAllSensors(vm)
    }

    val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mqttService = (service as SensorsMqttService.LocalBinder).service
            repository = SensorsRepository(mqttService!!)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mqttService = null
        }
    }

}