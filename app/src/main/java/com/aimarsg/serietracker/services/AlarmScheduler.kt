package com.aimarsg.serietracker.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import io.ktor.util.date.getTimeMillis

class AlarmScheduler(
    private val context: Context
){
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule() {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            //putExtra("EXTRA_MESSAGE", alarmItem.message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            getTimeMillis(),
            15*60*1000,// 15 minutos //AlarmManager.INTERVAL_DAY, // 15000
            pendingIntent
        )
        Log.e("Alarm", "Alarm set")
    }

    fun cancel() {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Log.e("Alarm", "Alarm canceled")
    }
}