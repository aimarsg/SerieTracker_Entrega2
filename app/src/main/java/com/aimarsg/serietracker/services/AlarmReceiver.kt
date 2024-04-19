package com.aimarsg.serietracker.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarm received")
        //val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        val channelId = "0"
        context?.let { ctx ->
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.icono_s)
                .setContentTitle("Alarm Demo")
                .setContentText("Notification sent with message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(1, builder.build())
        }

        // llamar al servicio que realiza la sincronizacion de datos
        if (context!=null) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    android.Manifest.permission.FOREGROUND_SERVICE
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("AlarmReceiver", "Lanzando servicio")
                val serviceIntent = Intent(context, UpdateService::class.java)
                context.startForegroundService(serviceIntent)
            } else {
                //Log.d("AlarmReceiver", "No hay permisos")
                Log.d("AlarmReceiver", "Lanzando servicio")
                val serviceIntent = Intent(context, UpdateService::class.java)
                context.startForegroundService(serviceIntent)
            }
        }

    }
}