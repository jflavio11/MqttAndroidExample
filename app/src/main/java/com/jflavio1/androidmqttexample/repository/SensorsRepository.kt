package com.jflavio1.androidmqttexample.repository

import android.util.Log
import com.jflavio1.androidmqttexample.model.CustomLightSensor
import com.jflavio1.androidmqttexample.mqtt.SensorsMqttService
import com.jflavio1.androidmqttexample.viewmodel.LightSensorViewModel
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.json.JSONObject

/**
 * SensorsRepository
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  10/5/17
 */
class SensorsRepository(val service: SensorsMqttService) {

    companion object {
        val GET_SENSORS = "GET_SENSORS"
        val UPDATE_LIGHT_STATE = "UPDATE_LIGHT_STATE"
    }

    /**
     * GetAllSensors is a method that will subscribe to a specific topic
     * "home_lights" on [SensorsMqttService.TOPICS] and when a message is received
     * on that topic it will classify the type of message getting the "type"
     * string from the MqttMessage.
     */
    fun getAllSensors(vm: LightSensorViewModel) {

        // we subscribe to sensors topic
        service.subscribeToTopic(SensorsMqttService.TOPICS[1], 0, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("Mqtt", "Success subscribing to topic ${SensorsMqttService.TOPICS[0]}")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("Mqtt", "Failure on topic subscription")
            }
        }, IMqttMessageListener { topic, message ->

            val msgObj = JSONObject(message.toString())
            val type = msgObj.getString(SensorsMqttService.MQTT_MESSAGE_TYPE)
            val payload = msgObj.getJSONObject(SensorsMqttService.MQTT_MESSAGE_PAYLOAD)

            when(type){

                "sensors_info" -> {
                    val array = payload.getJSONArray("sensors_info")
                    vm.updateSensorsInfo(SensorMapper().mapJsonArray(array))
                }

                "update_sensor" -> {
                    val obj = JSONObject(message.toString()).getJSONObject(SensorsMqttService.MQTT_MESSAGE_PAYLOAD).getJSONObject("sensor_info")
                    vm.updateSensor(SensorMapper().mapJson(obj))
                }

            }

        })

        val message = MqttMessage()
        val jsonMessage = JSONObject()
        jsonMessage.put(SensorsMqttService.MQTT_MESSAGE_TYPE, GET_SENSORS)
        message.qos = 0
        message.payload = jsonMessage.toString().toByteArray()

        // here we ask for home sensors information
        service.publish(SensorsMqttService.TOPICS[0], message)

    }

    /**
     * Used for turning on or turning off a light. When the MqttServer receive the message
     * and the light state is changed, we will be notified by the "update_sensor"
     * message type that is listened on [getAllSensors] method.
     */
    fun updateSensorState(customLightSensor: CustomLightSensor, turnedOn: Boolean){
        val message = MqttMessage()
        val jsonMessage = JSONObject()
        val sensorInfo = JSONObject()
        jsonMessage.put(SensorsMqttService.MQTT_MESSAGE_TYPE, UPDATE_LIGHT_STATE)

        sensorInfo.put("sensor_id", customLightSensor.id)
        sensorInfo.put("isTurnedOn", turnedOn)

        jsonMessage.put("sensor_info", sensorInfo)

        message.qos = 0
        message.payload = jsonMessage.toString().toByteArray()

        // here we ask for home sensors information
        service.publish(SensorsMqttService.TOPICS[0], message)

    }

}
