package com.jflavio1.androidmqttexample.views

import android.content.Context
import com.jflavio1.androidmqttexample.model.CustomLightSensor
import com.jflavio1.androidmqttexample.presenters.SensorsListPresenter

/**
 * SensorsListView
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
interface SensorsListView {

    fun setSensorPresenter(presenter: SensorsListPresenter)

    fun onMqttConnected()

    fun onMqttError(errorMessage: String)

    fun onMqttDisconnected()

    fun onMqttStopped()

    fun setSensorsTemperature(sensors: ArrayList<CustomLightSensor>)

    fun getViewContext() : Context

}