package com.jflavio1.androidmqttexample.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jflavio1.androidmqttexample.model.TempSensor

/**
 * TempSensorViewModel
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  9/5/17
 */
class TempSensorViewModel : ViewModel() {

    private lateinit var tempSensorsList: LiveData<ArrayList<TempSensor>>

    fun setSensorsList(list: ArrayList<TempSensor>){
        val data = MutableLiveData<ArrayList<TempSensor>>()
        data.value = list
        this.tempSensorsList = data
    }

    fun getSensors(): LiveData<ArrayList<TempSensor>> {
        return tempSensorsList
    }

}