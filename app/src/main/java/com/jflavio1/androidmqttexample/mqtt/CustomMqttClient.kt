package com.jflavio1.androidmqttexample.mqtt

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.IMqttToken

/**
 * CustomMqttClient
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
class CustomMqttClient(context: Context?, serverURI: String?, clientId: String?) : MqttAndroidClient(context, serverURI, clientId) {

    // Fixme: we should connect using mqttConnectOptions
    override fun connect(userContext: Any?, callback: IMqttActionListener?): IMqttToken {
        log("Connecting to Mqtt broker...")
        return super.connect(userContext, callback)
    }

    override fun subscribe(topic: String?, qos: Int, userContext: Any?, callback: IMqttActionListener?): IMqttToken {
        log("Subscribing to topic $topic")
        return super.subscribe(topic, qos, userContext, callback)
    }

    private fun log(text: String) {
        Log.d("MQTT", "Client: $text")
    }

}