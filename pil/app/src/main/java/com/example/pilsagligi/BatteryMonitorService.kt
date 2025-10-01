package com.example.pilsagligi

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder

class BatteryMonitorService : Service() {

    companion object {
        private const val CHANNEL_ID = "battery_monitor_channel"
        private const val NOTIF_ID = 1011
    }

    private var ringtone: Ringtone? = null
    private val prefs by lazy { getSharedPreferences("battery_prefs", Context.MODE_PRIVATE) }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
            val pct = if (level >= 0) (level * 100 / scale) else 0
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

            val low = prefs.getInt("low_threshold", 20)
            val high = prefs.getInt("high_threshold", 80)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

            if (pct <= low || (isCharging && pct >= high)) {
                playAlarm()
            } else {
                stopAlarm()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIF_ID, buildNotification())
        registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onDestroy() {
        super.onDestroy()
        kotlin.runCatching { unregisterReceiver(receiver) }
        stopAlarm()
    }

    private fun playAlarm() {
        if (ringtone == null) {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            ringtone = RingtoneManager.getRingtone(this, uri)
        }
        if (ringtone?.isPlaying != true) {
            ringtone?.play()
        }
    }

    private fun stopAlarm() {
        if (ringtone?.isPlaying == true) ringtone?.stop()
    }

    private fun buildNotification(): Notification {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(CHANNEL_ID, "Pil İzleme", NotificationManager.IMPORTANCE_LOW)
            nm.createNotificationChannel(ch)
        }
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            Notification.Builder(this)
        }
        return builder
            .setContentTitle("Pil izleme aktif")
            .setContentText("Eşik değerleri izleniyor")
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .build()
    }
}



