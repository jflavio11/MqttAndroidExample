package com.jflavio1.androidmqttexample.views

import com.jflavio1.androidmqttexample.model.TempSensor
import com.jflavio1.androidmqttexample.presenters.SensorsListPresenter

/**
 * SensorsListView
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
interface SensorsListView {

    fun setPresenter(presenter: SensorsListPresenter)

    fun setSensorsTemperature(sensors: ArrayList<TempSensor>)

}