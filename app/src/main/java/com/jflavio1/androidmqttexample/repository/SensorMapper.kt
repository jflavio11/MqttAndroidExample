package com.jflavio1.androidmqttexample.repository

import com.jflavio1.androidmqttexample.model.TempSensor
import org.json.JSONObject

/**
 * SensorMapper
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  10/5/17
 */
class SensorMapper() {

    fun mapJson(obj: JSONObject): TempSensor{
        return TempSensor(obj.getString("id"), obj.getString("name"), obj.getDouble("temperature"))
    }

}