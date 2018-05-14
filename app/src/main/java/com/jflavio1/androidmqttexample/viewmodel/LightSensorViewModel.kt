package com.jflavio1.androidmqttexample.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jflavio1.androidmqttexample.model.CustomLightSensor

/**
 * LightSensorViewModel
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  9/5/17
 */
class LightSensorViewModel : ViewModel() {

    private var tempSensorsList = MutableLiveData<ArrayList<CustomLightSensor>>()

    fun updateSensor(sensor: CustomLightSensor){
        for (i in 0..(tempSensorsList.value!!.size - 1)){
            if(tempSensorsList.value!![i].id == sensor.id){
                tempSensorsList.value!![i] = sensor
            }
        }
        this.tempSensorsList.postValue(tempSensorsList.value)
    }

    fun updateSensorsInfo(list: ArrayList<CustomLightSensor>){
        this.tempSensorsList.postValue(list)
    }

    fun getSensors(): LiveData<ArrayList<CustomLightSensor>> {
        return tempSensorsList
    }

}