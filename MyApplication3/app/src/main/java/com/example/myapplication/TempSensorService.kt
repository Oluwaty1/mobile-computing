package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.app.NotificationCompat

class TempSensorService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var tempSensor: Sensor? = null
    private val CHANNEL_ID = "temp_alert_channel"
    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        tempSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        startForeground(NOTIFICATION_ID, createNotification("Monitoring Temperature..."))
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val currentTemp = event?.values?.get(0) ?: 0f

        if (currentTemp < -15) {
            val alertNotification = createNotification("WARNING: Freezing! Temp is $currentTemp°C")
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(2, alertNotification)
        }
    }

    private fun createNotification(contentText: String): android.app.Notification {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Temperature Alerts",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Temperature Monitor")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}