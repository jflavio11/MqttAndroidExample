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

    fun getAllSensors(vm: LightSensorViewModel) {
        val message = MqttMessage()
        val jsonMessage = JSONObject()
        jsonMessage.put(SensorsMqttService.MQTT_MESSAGE_TYPE, GET_SENSORS)
        message.qos = 0
        message.payload = jsonMessage.toString().toByteArray()

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
            val type = msgObj.getString("type")
            val payload = msgObj.getJSONObject("payload")

            when(type){

                "sensors_info" -> {
                    val array = payload.getJSONArray("sensors_info")
                    vm.updateSensorsInfo(SensorMapper().mapJsonArray(array))
                }

                "update_sensor" -> {
                    val obj = JSONObject(message.toString()).getJSONObject("payload").getJSONObject("sensor_info")
                    vm.updateSensor(SensorMapper().mapJson(obj))
                }

            }

        })

        // here we ask for home sensors information
        service.publish(SensorsMqttService.TOPICS[0], message)

    }

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
        service.publish(SensorsMqttService.TOPICS[2], message)

    }

}