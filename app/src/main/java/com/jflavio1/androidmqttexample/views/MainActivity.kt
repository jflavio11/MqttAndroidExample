package com.jflavio1.androidmqttexample.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jflavio1.androidmqttexample.R
import com.jflavio1.androidmqttexample.model.TempSensor
import com.jflavio1.androidmqttexample.presenters.SensorsListPresenter
import com.jflavio1.androidmqttexample.presenters.SensorsListPresenterImpl

/**
 * MainActivity
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
class MainActivity : AppCompatActivity(), SensorsListView {

    lateinit var presenter : SensorsListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SensorsListPresenterImpl(this)
    }

    override fun setSensorPresenter(presenter: SensorsListPresenter) {
        this.presenter = presenter
        this.presenter.initMqttService()
    }

    override fun onMqttConnected() {
        this.presenter.getTemperatures()
    }

    override fun onMqttError(errorCode: Int) {
    }

    override fun onMqttDisconnected() {
    }

    override fun onMqttStopped() {
    }

    override fun setSensorsTemperature(sensors: ArrayList<TempSensor>) {

    }

    override fun getViewContext() = this

}
