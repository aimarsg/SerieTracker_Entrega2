package com.aimarsg.serietracker.utils

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.datetime.LocalDate
import java.util.Calendar
import java.util.TimeZone



/**
 * Añade un evento al calendario
 * @param context Contexto de la aplicación
 * @param title Título del evento
 * @param description Descripción del evento
 * @param startDate Fecha de inicio del evento (formato LocalDate)
 * @return ID del evento añadido
 */

fun addEventToCalendar(context: Context, title: String, description: String, startDate: LocalDate): Any {
    // COMPROBACIÓN DE PERMISOS DE CALENDARIIO, SI NO HAY PERMISOS NO SE PUEDE EJECUTAR


    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si no hay permisos, solicitarlos en tiempo de ejecución
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
                ),
                1
            )
            return@with
        }

        if (!comprobarPermisos(context)){
            return -1
        }

        // eliminar primero el evento anterior si existe    (la actualizacion se hace eliminando y creando uno nuevo)
        deleteEventFromCalendar(context, title)

        // obtener el id del calendario
        val calendarId = getCalendarId(context)

        // crear el evento
        val startMillis = Calendar.getInstance().run {
            set(startDate.year, startDate.monthNumber - 1, startDate.dayOfMonth, 10, 0)
            timeInMillis
        }
        Log.d(
            "Calendar",
            "Fecha: ${startDate.year}, ${startDate.monthNumber}, ${startDate.dayOfMonth} $startDate"
        )
        //val startMillis = startDate.epochUTCMilliseconds
        val endDate = startMillis + 60 * 60 * 1000 // 1 hora
        Log.d("Calendar", "Fecha en millis: $startMillis")
        val contentResolver: ContentResolver = context.contentResolver

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endDate)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            //put(CalendarContract.Events.EVENT_LOCATION, location)
            put(CalendarContract.Events.CALENDAR_ID, calendarId) // ID del calendario (OBLIGATIORIO)
            put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid")
        }
        Log.d("Calendar", "Creando evento")
        val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

        // Manejar el resultado si es necesario
        if (uri == null) {
            // Manejo de errores
            Log.d("Calendar", "Error al añadir evento")
            return -1
        } else {
            // El evento se ha añadido exitosamente
            val eventId = uri.lastPathSegment?.toLong()
            if (eventId != null) {
                // El evento se ha añadido exitosamente
                // añadir el recordatorio
                val values = ContentValues().apply {
                    put(CalendarContract.Reminders.MINUTES, 15)
                    put(CalendarContract.Reminders.EVENT_ID, eventId)
                    put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
                }
                val uri = contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values)
                Log.d("Calendar", "Recordatorio añadido con ID: $uri")
            }
            Log.d("Calendar", "Evento añadido con ID: $eventId")
            return eventId ?: -1
        }
    }
    return -1
}

private fun getCalendarId(context: Context): Long {
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    val EVENT_PROJECTION: Array<String> = arrayOf(
        CalendarContract.Calendars._ID,                     // 0
        CalendarContract.Calendars.ACCOUNT_NAME,            // 1
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   // 2
        CalendarContract.Calendars.OWNER_ACCOUNT            // 3
    )

    // The indices for the projection array above.
    val PROJECTION_ID_INDEX: Int = 0
    val PROJECTION_ACCOUNT_NAME_INDEX: Int = 1
    val PROJECTION_DISPLAY_NAME_INDEX: Int = 2
    val PROJECTION_OWNER_ACCOUNT_INDEX: Int = 3

    // Run query
    val uri: Uri = CalendarContract.Calendars.CONTENT_URI
    val selection: String = "(" +
            "${CalendarContract.Calendars.ACCOUNT_TYPE} = ?)"+"AND (${CalendarContract.Calendars.ACCOUNT_NAME} = ${CalendarContract.Calendars.OWNER_ACCOUNT})"
    val selectionArgs: Array<String> = arrayOf("com.google")
    val cur: Cursor? = context.contentResolver.query(uri, EVENT_PROJECTION, selection, selectionArgs, null)
    // Use the cursor to step through the returned records
    if (cur != null) {
        while (cur.moveToNext()) {
            // Get the field values
            val calID: Long = cur.getLong(PROJECTION_ID_INDEX)
            val displayName: String = cur.getString(PROJECTION_DISPLAY_NAME_INDEX)
            val accountName: String = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX)
            val ownerName: String = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX)
            Log.d("Calendar", "ID: $calID, Display Name: $displayName, Account Name: $accountName, Owner Name: $ownerName")
            return calID
        }
    }else{
        Log.d("Calendar", "No se encontró ningún calendario")
        return -1
    }
    return -1
}


/**
 * Elimina un evento del calendario
 * @param context Contexto de la aplicación
 * @param title Título del evento a eliminar
 */
fun deleteEventFromCalendar(context: Context, title: String) {
    val contentResolver: ContentResolver = context.contentResolver

    // Definir la selección (condición)
    val selection = "${CalendarContract.Events.TITLE} = ?"
    // Definir los argumentos de la selección
    val selectionArgs = arrayOf(title)

    // Realizar la eliminación del evento
    val uri = CalendarContract.Events.CONTENT_URI
    val rowsDeleted = contentResolver.delete(uri, selection, selectionArgs)

    // Verificar si se eliminó algún evento
    if (rowsDeleted > 0) {
        // El evento se ha eliminado exitosamente
    } else {
        // No se encontró ningún evento con ese título
    }
}


fun comprobarPermisos(context: Context): Boolean {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CALENDAR
        ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CALENDAR
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return false
    }else{
        return true
    }

}