package com.aimarsg.serietracker.services


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.model.repositories.CatalogoRepository
import com.aimarsg.serietracker.model.repositories.TrackerRepository
import com.aimarsg.serietracker.model.webclient.APIClient
import com.aimarsg.serietracker.model.webclient.AuthenticationException
import com.aimarsg.serietracker.ui.isNetworkAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@AndroidEntryPoint
class UpdateService : Service() {

    @Inject
    lateinit var catalogoRepository: CatalogoRepository
    @Inject
    lateinit var trackerRepository: TrackerRepository
    @Inject
    lateinit var ApiClient: APIClient


    val context = this
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("UpdateService", "Service started")
        startForeground(1, createNotification())
        serviceScope.launch {
            supervisorScope {
                try {
                    siconizarDatos()
                    val successNotification = createSuccessNotification()
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(1, successNotification)
                } catch (e: Exception) {
                    Log.e("UpdateService", "Error al sincronizar datos", e)
                    errorNotification("Error al sincronizar datos")
                    val errorNotification = createErrorNotification()
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(1, errorNotification)
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("UpdateService", "Service destroyed")
    }

    suspend fun siconizarDatos() {
        Log.d("UpdateService", "Sincronizando datos")
        if (!isNetworkAvailable(context)) {
            errorNotification(context.getString(R.string.no_internet))
        } else {
            try {
                val seriesUsuario = trackerRepository.getAllSeries()
                ApiClient.uploadUserData(seriesUsuario)
                trackerRepository.updateSeriesUsuario()
                catalogoRepository.updateCatalogo()
            } catch (ae: AuthenticationException){
                Log.e("UpdateService", "No se ha iniciado sesion")
                errorNotification("No has iniciado sesion")
            } catch (e: Exception) {
                Log.e("UpdateService", "Error al sincronizar datos", e)
                errorNotification("Error al sincronizar datos")
            }
        }
        /*if (!isNetworkAvailable(context)) {
            errorNotification(context.getString(R.string.no_internet))
        }else {
            try {
                serviceScope.launch(Dispatchers.IO) {
                    val seriesUsuario = trackerRepository.getAllSeries()
                    ApiClient.uploadUserData(seriesUsuario)
                    trackerRepository.updateSeriesUsuario()
                    catalogoRepository.updateCatalogo()
                }
            } catch (e: Exception) {
                Log.e("UpdateService", "Error al sincronizar datos", e)
                errorNotification(context.getString(R.string.no_internet))
            }
        }*/

    }

    private fun createNotification(): Notification {
        val notificationChannelId = "0"
        // Configuración del canal de notificación y creación de la notificación
        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Sincronización en Progreso")
            .setContentText("Tus datos se están sincronizando.")
            .setSmallIcon(R.drawable.icono_s)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }
    private fun createSuccessNotification(): Notification {
        val notificationChannelId = "Task_channel"
        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Sincronización Completada")
            .setContentText("La sincronización ha terminado exitosamente.")
            .setSmallIcon(R.drawable.icono_s)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createErrorNotification(): Notification {
        val notificationChannelId = "Task_channel"
        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Error")
            .setContentText("Hubo un error durante la sincronización.")
            .setSmallIcon(R.drawable.icono_s)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun errorNotification(s: String) {
        // show error notification

        val notificationManager = getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(this, "0")
            .setContentTitle("Error")
            .setContentText(s)
            .setSmallIcon(R.drawable.icono_s)
            .build()
        notificationManager.notify(0, notification)

    }
}