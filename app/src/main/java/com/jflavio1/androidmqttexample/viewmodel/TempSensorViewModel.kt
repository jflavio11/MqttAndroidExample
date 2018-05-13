package com.jflavio1.androidmqttexample.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jflavio1.androidmqttexample.model.CustomLightSensor

/**
 * TempSensorViewModel
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  9/5/17
 */
class TempSensorViewModel : ViewModel() {

    private var tempSensorsList = MutableLiveData<ArrayList<CustomLightSensor>>()

    fun updateSensorsInfo(list: ArrayList<CustomLightSensor>){
        this.tempSensorsList.postValue(list)
    }

    fun getSensors(): LiveData<ArrayList<CustomLightSensor>> {
        return tempSensorsList
    }

}