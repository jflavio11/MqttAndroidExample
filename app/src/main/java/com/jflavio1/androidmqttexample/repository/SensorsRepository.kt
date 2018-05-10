package com.jflavio1.androidmqttexample.repository

import android.util.Log
import com.jflavio1.androidmqttexample.model.TempSensor
import com.jflavio1.androidmqttexample.mqtt.SensorsMqttService
import com.jflavio1.androidmqttexample.viewmodel.TempSensorViewModel
import org.eclipse.paho.client.mqttv3.IMqttActionListener
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
    }

    fun getAllSensors(vm: TempSensorViewModel) {
        /*service.mqttClient.subscribe("temperatures", 0) { topic, message ->

            val m = JSONObject(message.toString())
            val payload = m.getJSONObject("payload")
            val array = payload.getJSONArray("sensors")

            val mapper = SensorMapper()
            val list = ArrayList<TempSensor>()

            for (i in 0..(array.length()-1)){
                list.add(mapper.mapJson(array.getJSONObject(i)))
            }

            vm.setSensorsList(list)

        }*/

        val message = MqttMessage()
        val jsonMessage = JSONObject()
        jsonMessage.put("type", "get_sensors")
        message.qos = 0
        message.payload = jsonMessage.toString().toByteArray()

        service.mqttClient.publish("sensors", message, service, object: IMqttActionListener{
            override fun onSuccess(asyncActionToken: IMqttToken?) {
               Log.d("MqttRepository", "Message get_sensors was sent to topic sensors")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("MqttRepository", "Fail on sending get_sensors message")
            }

        })
    }

}