package com.jflavio1.androidmqttexample.presenters

import com.jflavio1.androidmqttexample.model.CustomLightSensor

/**
 * SensorsListPresenter
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
interface SensorsListPresenter {

    fun initMqttService()

    fun stopMqttService()

    fun getTemperatures()

    fun changeLightState(sensor: CustomLightSensor, turnedOn: Boolean)

}