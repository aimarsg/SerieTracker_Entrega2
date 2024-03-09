package com.aimarsg.serietracker.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.aimarsg.serietracker.data.Idioma
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CambioDeIdioma @Inject constructor() {

    var idiomaActual: Idioma = when (Locale.getDefault().language.lowercase()){
        "es" -> Idioma.Castellano
        "en" -> Idioma.English
        "eu" -> Idioma.Euskera
        else -> {
            Idioma.Castellano}
    }

    fun cambiarIdioma(idioma: Idioma, context: Context, recreate: Boolean = false) {

        if (idioma != idiomaActual || idiomaActual.codigo != Locale.getDefault().language) {

            /*context.resources.apply {
                val locale = Locale(idioma.codigo)
                val config = Configuration(configuration)

                context.createConfigurationContext(configuration)
                Locale.setDefault(locale)
                config.setLocale(locale)

                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, displayMetrics)
            }*/

            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(idioma.codigo))

            idiomaActual = idioma
            //if (recreate) context.getActivity()?.recreate()

        }
    }

    fun Context.getActivity(): ComponentActivity? = when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> null
    }
}