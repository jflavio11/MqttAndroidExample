package com.jflavio1.androidmqttexample.repository

import com.jflavio1.androidmqttexample.model.CustomLightSensor
import org.json.JSONArray
import org.json.JSONObject

/**
 * SensorMapper
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  10/5/17
 */
class SensorMapper() {

    fun mapJsonArray(array: JSONArray): ArrayList<CustomLightSensor>{
        val list = arrayListOf<CustomLightSensor>()
        for(i in 0..(array.length() -1)){
            list.add(mapJson(array.getJSONObject(i)))
        }
        return list
    }

    fun mapJson(obj: JSONObject): CustomLightSensor{
        return CustomLightSensor(obj.getString("id"), obj.getString("name"), obj.getBoolean("turnedOn"))
    }

}