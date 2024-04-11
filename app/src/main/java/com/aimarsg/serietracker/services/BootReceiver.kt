package com.aimarsg.serietracker.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Volver a programar la tarea periodica tras reiniciar el dispositivo
            val alarmScheduler = AlarmScheduler(context)
            alarmScheduler.schedule()
        }
    }
}

