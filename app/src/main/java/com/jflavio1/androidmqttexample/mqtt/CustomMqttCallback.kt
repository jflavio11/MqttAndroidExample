package com.jflavio1.androidmqttexample.mqtt

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage

/**
 * CustomMqttCallback
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  10/5/17
 */
class CustomMqttCallback(val context: Context) : MqttCallbackExtended {

    override fun connectComplete(reconnect: Boolean, serverURI: String?) {
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        log("Arrived message on topic $topic")
    }

    override fun connectionLost(cause: Throwable?) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(SensorsMqttService.CONNECTION_LOST))
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
    }

    private fun log(text: String) {
        Log.d("MQTT", text)
    }
}