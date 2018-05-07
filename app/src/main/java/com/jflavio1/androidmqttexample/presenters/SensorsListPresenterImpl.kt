package com.jflavio1.androidmqttexample.presenters

import com.jflavio1.androidmqttexample.views.SensorsListView

/**
 * SensorsListPresenterImpl
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
class SensorsListPresenterImpl(val view: SensorsListView) : SensorsListPresenter {

    init {
        this.view.setSensorPresenter(this)
    }

    override fun initMqttService() {
        // TODO init mqtt service
    }

    override fun getTemperatures() {
        // TODO call MQTT
    }

}