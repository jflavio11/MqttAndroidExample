package com.jflavio1.androidmqttexample.presenters

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.os.Build
import android.util.Log
import com.firebase.jobdispatcher.*
import com.jflavio1.androidmqttexample.mqtt.SensorsMqttService
import com.jflavio1.androidmqttexample.views.SensorsListView


/**
 * SensorsListPresenterImpl
 *
 * @author Jose Flavio - jflavio90@gmail.com
 * @since  6/5/17
 */
class SensorsListPresenterImpl(val view: SensorsListView) : SensorsListPresenter {

    private val TAG = "SensorsPresenter"

    init {
        this.view.setSensorPresenter(this)
    }

    override fun initMqttService() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(this.view.getViewContext()))

            val job = dispatcher.newJobBuilder()
                    // persist the task across boots
                    .setLifetime(Lifetime.FOREVER)
                    // Call this service when the criteria are met.
                    .setService(SensorsMqttService::class.java)
                    // unique id of the task
                    .setTag("OneTimeJob")
                    // We are mentioning that the job is not periodic.
                    .setRecurring(false)
                    // Run between 30 - 60 seconds from now.
                    .setTrigger(Trigger.executionWindow(30, 60))
                    //Run this job only when the network is avaiable.
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build()

            dispatcher.mustSchedule(job)

        } else {

            val componentName = ComponentName(this.view.getViewContext(), SensorsMqttService::class.java)
            val jobInfo = JobInfo.Builder(12, componentName)
                    .setRequiresCharging(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .build()

            val jobScheduler = this.view.getViewContext().getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            val resultCode = jobScheduler.schedule(jobInfo)
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d(TAG, "Job scheduled!")
            } else {
                Log.d(TAG, "Job not scheduled")
            }

        }
    }

    override fun getTemperatures() {
        // TODO call MQTT
    }

}