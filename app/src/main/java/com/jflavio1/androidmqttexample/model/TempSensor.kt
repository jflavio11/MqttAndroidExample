package com.jflavio1.androidmqttexample.model

/**
 * TempSensor
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
class TempSensor(var id: String, var name: String, var temperature: Double) {

    fun obtainStringTemperature() = {"$temperature °C"}

}