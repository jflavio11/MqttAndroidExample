package com.jflavio1.androidmqttexample.mqtt

import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
/**
 * SensorsMqttService
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
class SensorsMqttService : JobService(), BaseMqttModel {

    lateinit var mqttClient: CustomMqttClient
    lateinit var mqttCliendId: String

    companion object {
        val MQTT_START_REASON = "reason"
        val MQTT_CONNECT = "mqtt_connect"
        val MQTT_DISCONNECT = "mqtt_disconnect"

        val MQTT_SERVER_URL = "broker.hivemq.com"
    }

    // TODO have a VIEW representation here?

    override fun onStartJob(params: JobParameters?): Boolean {
        this.mqttCliendId = Build.SERIAL

        if (MQTT_CONNECT == params!!.extras!!.getString(MQTT_START_REASON)) {
            connectToServer()
        } else if (MQTT_DISCONNECT == params.extras!!.getString(MQTT_START_REASON)) {
            disconnectFromServer()
        }

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun connectToServer() {
        mqttClient = CustomMqttClient(this, MQTT_SERVER_URL, this.mqttCliendId)
        mqttClient.connect(this, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                logMqtt("Success connecting to server... now subscribe to receive messages")
                subscribeToTopic("temperatures")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                logErrorMqtt("Error on connecting to server... retry?")
                // TODO warn on UI for reconnect?
            }
        })
    }

    override fun disconnectFromServer() {
        this.mqttClient.disconnect()
    }

    override fun subscribeToTopic(topicName: String) {

        this.mqttClient.subscribe(topicName, 0, this, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                logMqtt("Success subscribing to topic $topicName")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                logMqtt("Failure on topic subscription")
            }
        }) { topic, message -> }

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

}