package com.jflavio1.androidmqttexample.mqtt

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import org.eclipse.paho.client.mqttv3.*
import org.json.JSONObject


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
        val MQTT_CONNECT = "mqtt_connect"
        val MQTT_DISCONNECT = "mqtt_disconnect"

        val MQTT_SERVER_URL = "tcp://broker.mqttdashboard.com:1883"

        // connection state filters
        val CONNECTION_SUCCESS = "CONNECTION_SUCCESS"
        val CONNECTION_FAILURE = "CONNECTION_FAILURE"
        val CONNECTION_LOST = "CONNECTION_LOST"
        val DISCONNECT_SUCCESS = "DISCONNECT_SUCCESS"

        val MQTT_MESSAGE_TYPE = "type"
        val MQTT_MESSAGE_PAYLOAD = "payload"

        val TOPICS = arrayOf("home_sensors_info", "home_lights")
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // TODO create notification for showing on Android Oreo: "You are listening to temperature changes on real time"
            startForeground(0, Notification())
        }
        logMqtt("Created mqtt service...")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // TODO getting Build.SERIAL will no work on Android P
        this.mqttCliendId = Build.SERIAL

        if(intent == null) {
            logErrorMqtt("Null intent in onStartCommand, returning START_NOT_STICKY")
            stopSelf()
            return Service.START_NOT_STICKY
        }

        if (MQTT_CONNECT == intent.action!!) {
            connectToServer()
        } else if (MQTT_DISCONNECT == intent.action) {
            disconnectFromServer()
        }

        // this will return START_STICKY and will recreate the service (with saved start intent) when Android destroys it
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * Connects to the Mqtt Server.
     */
    override fun connectToServer() {
        mqttClient = CustomMqttClient(this, MQTT_SERVER_URL, this.mqttCliendId)
        mqttClient.setCallback(CustomMqttCallback(this))

        // enable logs from library
        mqttClient.setTraceEnabled(true)
        mqttClient.setTraceCallback(CustomMqttLog())

        val options = MqttConnectOptions()
        options.apply {
            connectionTimeout = 30
            isAutomaticReconnect = true
            isCleanSession = true
            keepAliveInterval = 120
        }

        mqttClient.connect(options,this, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                logMqtt("Success connecting to server...")
                LocalBroadcastManager.getInstance(this@SensorsMqttService).sendBroadcast(Intent(CONNECTION_SUCCESS))
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                logErrorMqtt("Error on connecting to server... retry?: ${exception!!.toString()}")
                LocalBroadcastManager.getInstance(this@SensorsMqttService).sendBroadcast(Intent(CONNECTION_FAILURE))
                disconnectFromServer()
            }
        })
    }

    /**
     * Disconnect from the Mqtt Server.
     */
    override fun disconnectFromServer() {
        this.mqttClient.disconnect(this, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                logMqtt("Disconnected from server attempt success...")
                mqttClient.unregisterResources()
                SensorsMqttService@ mqttClient.close()
                LocalBroadcastManager.getInstance(this@SensorsMqttService).sendBroadcast(Intent(DISCONNECT_SUCCESS))
                stopSelf()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                logMqtt("Failure on triying to disconnect: ${exception.toString()}")
            }

        })
    }

    override fun subscribeToTopic(topicName: String, qos: Int, subscriptionListener: IMqttActionListener?, messageListener: IMqttMessageListener?) {
        logMqtt("Subscribing to topic $topicName")
        this.mqttClient.subscribe(topicName, qos, this, subscriptionListener, messageListener)
    }

    override fun unsubscribeFromTopic(topicName: String) {
        this.mqttClient.unsubscribe(topicName)
    }

    /**
     * Publish a [MqttMessage] into the specified [topicName].
     */
    fun publish(topicName: String, message: MqttMessage){
        this.mqttClient.publish(topicName, message, this, object: IMqttActionListener{
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                logMqtt("Message was sent to topic $topicName")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("MqttRepository", "Fail on sending message to topic $topicName")
            }

        })
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