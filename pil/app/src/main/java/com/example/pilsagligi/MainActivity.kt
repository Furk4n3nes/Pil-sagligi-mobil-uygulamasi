package com.example.pilsagligi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvBatteryPercent: TextView
    private lateinit var tvVoltage: TextView
    private lateinit var tvCurrent: TextView
    private lateinit var tvTemp: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvLowLabel: TextView
    private lateinit var tvHighLabel: TextView
    private lateinit var tvMonitorState: TextView
    private lateinit var seekLow: SeekBar
    private lateinit var seekHigh: SeekBar

    private val prefs by lazy { getSharedPreferences("battery_prefs", Context.MODE_PRIVATE) }

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
            val pct = if (level >= 0) (level * 100 / scale) else 0
            val voltageMv = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
            val tempTenthC = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

            tvBatteryPercent.text = "Anlık Pil: ${pct}%"
            tvVoltage.text = String.format("%.1fV", voltageMv / 1000.0)

            // Current is not reliably provided via broadcast; try BatteryManager
            val bm = getSystemService(BATTERY_SERVICE) as BatteryManager
            val microAmps = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
            if (microAmps != Int.MIN_VALUE && microAmps != 0) {
                val amps = Math.abs(microAmps) / 1_000_000.0
                tvCurrent.text = String.format("%,.0fA", amps * 1000) // mimic screenshot format
            } else {
                tvCurrent.text = "-"
            }

            tvTemp.text = String.format("%.1f°C", tempTenthC / 10.0)
            tvStatus.text = when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> "Şarj Oluyor"
                BatteryManager.BATTERY_STATUS_FULL -> "Tam Dolu"
                else -> "Deşarj Oluyor"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvBatteryPercent = findViewById(R.id.tvBatteryPercent)
        tvVoltage = findViewById(R.id.tvVoltage)
        tvCurrent = findViewById(R.id.tvCurrent)
        tvTemp = findViewById(R.id.tvTemp)
        tvStatus = findViewById(R.id.tvStatus)
        tvLowLabel = findViewById(R.id.tvLowLabel)
        tvHighLabel = findViewById(R.id.tvHighLabel)
        tvMonitorState = findViewById(R.id.tvMonitorState)
        seekLow = findViewById(R.id.seekLow)
        seekHigh = findViewById(R.id.seekHigh)

        val low = prefs.getInt("low_threshold", 20)
        val high = prefs.getInt("high_threshold", 80)
        seekLow.progress = low
        seekHigh.progress = high
        tvLowLabel.text = "Düşük Eşik: ${low}%"
        tvHighLabel.text = "Yüksek Eşik: ${high}%"

        seekLow.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvLowLabel.text = "Düşük Eşik: ${progress}%"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                prefs.edit().putInt("low_threshold", seekLow.progress).apply()
            }
        })

        seekHigh.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvHighLabel.text = "Yüksek Eşik: ${progress}%"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                prefs.edit().putInt("high_threshold", seekHigh.progress).apply()
            }
        })

        findViewById<android.view.View>(R.id.btnStart).setOnClickListener {
            tvMonitorState.text = "İzleme aktif"
            startService(Intent(this, BatteryMonitorService::class.java))
        }
        findViewById<android.view.View>(R.id.btnStop).setOnClickListener {
            tvMonitorState.text = "İzleme pasif"
            stopService(Intent(this, BatteryMonitorService::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onStop() {
        super.onStop()
        kotlin.runCatching { unregisterReceiver(batteryReceiver) }
    }
}