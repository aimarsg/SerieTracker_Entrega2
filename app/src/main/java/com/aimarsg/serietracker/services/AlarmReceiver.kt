package com.aimarsg.serietracker.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
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

        /* TODO
          * hacer que al recibir la alarma se llame al metodo de
          * actualizar los datos con la api
          */

        /* TODO
          * hacer que al reiniciar el dispositivo se restauren las alarmas
         */

    }
}