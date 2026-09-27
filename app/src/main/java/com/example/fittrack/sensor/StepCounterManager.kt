package com.example.fittrack.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

/**
 * Hardware integration: reads the device's built-in step-counter sensor
 * (TYPE_STEP_COUNTER) and exposes a live step count to Compose UI.
 */
class StepCounterManager(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private var initialStepCount: Int? = null
    private val _steps = mutableStateOf(0)
    val steps: State<Int> get() = _steps

    val isSensorAvailable: Boolean get() = stepSensor != null

    fun start() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSinceReboot = event.values[0].toInt()
            if (initialStepCount == null) {
                initialStepCount = totalSinceReboot
            }
            _steps.value = totalSinceReboot - (initialStepCount ?: totalSinceReboot)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op: accuracy changes don't affect step counting logic here.
    }
}
