package com.jflavio1.androidmqttexample.mqtt

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import org.eclipse.paho.client.mqttv3.*


/**
 * SensorsMqttService
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
class SensorsMqttService : Service(), BaseMqttModel {

    lateinit var mqttClient: CustomMqttClient
    lateinit var mqttCliendId: String

    companion object {
        val MQTT_START_REASON = "reason"
        val MQTT_CONNECT = "mqtt_connect"
        val MQTT_DISCONNECT = "mqtt_disconnect"

        val MQTT_SERVER_URL = "tcp://broker.mqttdashboard.com:1883"
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // TODO create notificaction for showing on Android Oreo: "You are listening to temperature changes on real time"
            startForeground(0, Notification())
        }
        logMqtt("Created mqtt service...")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // This will no work on Android P
        this.mqttCliendId = Build.SERIAL

        if (MQTT_CONNECT == intent!!.action!!) {
            connectToServer()
        } else if (MQTT_DISCONNECT == intent.action) {
            disconnectFromServer()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun connectToServer() {
        mqttClient = CustomMqttClient(this, MQTT_SERVER_URL, this.mqttCliendId)

        mqttClient.setCallback(object: MqttCallbackExtended{
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                logMqtt("Arrived message at topic $topic\n${message.toString()}")
            }

            override fun connectionLost(cause: Throwable?) {
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
            }

        })

        mqttClient.connect(this, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                logMqtt("Success connecting to server... now subscribe to receive messages")
                subscribeToTopic("temperatures")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                logErrorMqtt("Error on connecting to server... retry?: ${exception!!.toString()}")
                LocalBroadcastManager.getInstance(this@SensorsMqttService).sendBroadcast(Intent("CONNECTION_FAILURE"))
                disconnectFromServer()
                // TODO warn on UI for reconnect?
            }
        })
    }

    override fun disconnectFromServer() {
        this.mqttClient.disconnect(this, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                logMqtt("Disconnected from server attempt success...")
                SensorsMqttService@ mqttClient.close()
                LocalBroadcastManager.getInstance(this@SensorsMqttService).sendBroadcast(Intent("DISCONNECT_SUCCESS"))
                stopSelf()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                logMqtt("Failure on triying to disconnect: ${exception.toString()}")
            }

        })
    }

    override fun subscribeToTopic(topicName: String) {

        this.mqttClient.subscribe(topicName, 0, this, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                logMqtt("Success subscribing to topic $topicName")
                LocalBroadcastManager.getInstance(this@SensorsMqttService).sendBroadcast(Intent("CONNECTION_SUCCESS"))
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                logMqtt("Failure on topic subscription")
                LocalBroadcastManager.getInstance(this@SensorsMqttService).sendBroadcast(Intent("CONNECTION_FAILURE"))
                disconnectFromServer()
            }
        })
    }

    override fun unsubscribeFromTopic(topicName: String) {
        // TODO
    }

    private fun logMqtt(text: String) {
        Log.d("MQTT", text)
    }

    private fun logErrorMqtt(text: String) {
        Log.w("MQTT", text)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true)
        }
    }

    override fun onBind(intent: Intent?) = mBinder

    private val mBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        val service: SensorsMqttService
            get() = this@SensorsMqttService
    }

}