package com.aimarsg.serietracker.ui

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.model.entities.SerieUsuario
import com.aimarsg.serietracker.model.repositories.TrackerRepository
import com.aimarsg.serietracker.ui.theme.md_theme_light_onPrimary
import com.aimarsg.serietracker.ui.theme.md_theme_light_primary
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

object SerieTrackerWidget : GlanceAppWidget() {
    
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                Contenido(context)
            }
        }
    }

    @Composable
    fun Contenido(context: Context) {


        val prefs = currentState<Preferences>()
        val data : String? = prefs[WidgetReceiver.series]
        val seriesSiguiendo: List<SerieUsuario> = if (data != null) Json.decodeFromString(data) else emptyList()
        val listaOrdenada = seriesSiguiendo.sortedByDescending { it.valoracion }
        val seriesSiguiendoOrdenado = listaOrdenada.take(5)


        Column(
            modifier = GlanceModifier.fillMaxSize()
                .background(GlanceTheme.colors.background),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Log.d("Widget", "num Series: ${seriesSiguiendo.size}")

            //Text(text = "Número de series siguiendo: ${seriesSiguiendo.size}", modifier = GlanceModifier.padding(12.dp))

            Row(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = GlanceModifier.background(md_theme_light_primary).fillMaxWidth().padding(4.dp)
            ) {
                Text(text = context.getString(R.string.seriesFavoritas), modifier = GlanceModifier.padding(6.dp),
                    style = TextStyle(color = ColorProvider(md_theme_light_onPrimary)))
                Image(
                    provider = ImageProvider(R.drawable.baseline_home_24),
                    contentDescription = null,
                    modifier = GlanceModifier.size(30.dp).clickable(actionStartActivity<MainActivity>()).padding(6.dp)
                )
            }

            when{
                seriesSiguiendoOrdenado.isEmpty() -> {
                    Text(text = context.getString(R.string.no_info_widget), modifier = GlanceModifier.padding(12.dp))
                }
                else -> {
                    LazyColumn {
                        items(seriesSiguiendoOrdenado) {
                            Row(
                                modifier = GlanceModifier.fillMaxWidth().padding(4.dp)
                            ) {
                                Text(text = it.titulo, modifier = GlanceModifier.padding(horizontal = 6.dp, vertical = 3.dp))
                                Text(text = it.valoracion.toString(), modifier = GlanceModifier.padding(horizontal = 6.dp, vertical = 3.dp))
                                Image(
                                    provider = ImageProvider(R.drawable.baseline_star_24), contentDescription = null,
                                    modifier = GlanceModifier.size(23.dp).padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }

                        }
                    }
                }
            }
        }
    }

}

@AndroidEntryPoint
class WidgetReceiver : GlanceAppWidgetReceiver() {
    
    override val glanceAppWidget: GlanceAppWidget = SerieTrackerWidget
    private val coroutineScope = MainScope()
    
    // Inject the data repository
    @Inject
    lateinit var trackerRepository: TrackerRepository
    
    // function to update the widget
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateData(context)
    }

    private fun updateData(context: Context) {
        coroutineScope.launch {
            Log.d("Widget", "Actualizando datos")

            val seriesSiguiendo = trackerRepository.getSeriesSiguiendo().first()

            GlanceAppWidgetManager(context).getGlanceIds(SerieTrackerWidget::class.java).forEach {glanceId ->
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId){widgetDataStore ->
                    widgetDataStore.toMutablePreferences().apply {
                        this[series] = Json.encodeToString(seriesSiguiendo)
                    }
                }
            }

            glanceAppWidget.updateAll(context)
        }
    }
    companion object{
        val series = stringPreferencesKey("series")
    }

}