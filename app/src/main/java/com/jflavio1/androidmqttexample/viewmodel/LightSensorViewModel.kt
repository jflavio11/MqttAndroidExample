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

    /**
     * UpdateSensor is a method that is going to receive a [CustomLightSensor] object
     * from [com.jflavio1.androidmqttexample.repository.SensorsRepository] and it's going
     * to search the sensor on the sensors list and update it.
     */
    fun updateSensor(sensor: CustomLightSensor){
        for (i in 0..(tempSensorsList.value!!.size - 1)){
            if(tempSensorsList.value!![i].id == sensor.id){
                tempSensorsList.value!![i] = sensor
            }
        }
        this.tempSensorsList.postValue(tempSensorsList.value)
    }

    /**
     * UpdateSensorsInfo is a method that given an [ArrayList] of [CustomLightSensor]
     * will update all list.
     */
    fun updateSensorsInfo(list: ArrayList<CustomLightSensor>){
        this.tempSensorsList.postValue(list)
    }

    /**
     * GetSensors will return the current [ArrayList] as a [LiveData] object.
     */
    fun getSensors(): LiveData<ArrayList<CustomLightSensor>> {
        return tempSensorsList
    }

}