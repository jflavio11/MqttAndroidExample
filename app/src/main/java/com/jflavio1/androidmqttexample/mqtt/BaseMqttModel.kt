package com.jflavio1.androidmqttexample.mqtt

import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.jetbrains.annotations.Nullable

/**
 * BaseMqttModel
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
interface BaseMqttModel {

    fun connectToServer()

    fun disconnectFromServer()

    fun subscribeToTopic(topicName: String, qos: Int, subscriptionListener: IMqttActionListener?, messageListener: IMqttMessageListener?)

    fun unsubscribeFromTopic(topicName: String)

}