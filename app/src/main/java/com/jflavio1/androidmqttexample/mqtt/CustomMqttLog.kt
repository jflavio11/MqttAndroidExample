package com.jflavio1.androidmqttexample.mqtt

import android.util.Log
import org.eclipse.paho.android.service.MqttTraceHandler
import java.lang.Exception

/**
 * CustomMqttLog
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  16/5/17
 */
class CustomMqttLog : MqttTraceHandler{
    private val TAG = "MQTT"
    override fun traceDebug(tag: String?, message: String?) {
        Log.d(TAG, "$tag - $message")
    }

    override fun traceException(tag: String?, message: String?, e: Exception?) {
        Log.d(TAG, "$tag - $message \n${e.toString()}")
    }

    override fun traceError(tag: String?, message: String?) {
        Log.d(TAG, "$tag - $message")
    }

}