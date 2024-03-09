package com.aimarsg.serietracker.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimarsg.serietracker.data.Idioma
import com.aimarsg.serietracker.data.MyPreferencesDataStore
import com.aimarsg.serietracker.data.entities.SerieCatalogo
import com.aimarsg.serietracker.data.entities.SerieUsuario
import com.aimarsg.serietracker.data.repositories.CatalogoRepository
import com.aimarsg.serietracker.data.repositories.TrackerRepository
import com.aimarsg.serietracker.utils.CambioDeIdioma
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.first

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val myPreferencesDataStore: MyPreferencesDataStore,
    private val catalogoRepository: CatalogoRepository,
    private val trackerRepository: TrackerRepository,
    private val cambioDeIdioma: CambioDeIdioma
): ViewModel() {
    val tema = myPreferencesDataStore.preferencesStatusFlow.map {
        it.temaClaro
    }
    val idioma = myPreferencesDataStore.preferencesStatusFlow.map {
        it.idioma
    }

    val idiomaActual by cambioDeIdioma::idiomaActual

    val seriesCatalogo = catalogoRepository.getAllSeries()

    val seriesSiguiendo = trackerRepository.getSeriesSiguiendo()
    val seriesPendiente = trackerRepository.getSeriesPendiente()

    var serieSeleccionada by mutableStateOf(SerieCatalogo("", 0, ""))

    fun updateTheme(theme: Boolean){
        viewModelScope.launch {
            myPreferencesDataStore.updateTheme(theme)
        }
    }

    fun updateIdioma(idioma: Idioma, context: Context){
        cambioDeIdioma.cambiarIdioma(idioma, context)
        viewModelScope.launch {
            myPreferencesDataStore.updateIdioma(idioma)
        }
    }

    fun reloadLang(idioma: Idioma, context: Context) = cambioDeIdioma.cambiarIdioma(idioma, context, false)

    fun addSerie(serie: SerieUsuario) {
        viewModelScope.launch {
            trackerRepository.addSerie(serie)
        }
    }
    fun eliminarSerie(serie: SerieUsuario){
        viewModelScope.launch {
            trackerRepository.deleteSerie(serie)
        }
    }
    fun editarSerie(serie: SerieUsuario) {
        viewModelScope.launch {
            trackerRepository.updateSerie(serie)
        }
    }

    fun seriesSiguiendoToJson(): String {
        val gsonBuilder = GsonBuilder().setPrettyPrinting().create()

        return runBlocking {
            val seriesSiguiendo = trackerRepository.getSeriesSiguiendo().first()
                .map { serieUsuario ->
                    mapOf(
                        "titulo" to serieUsuario.titulo,
                        "numero temporadas" to serieUsuario.numTemps,
                        "episodio actual" to serieUsuario.epActual.toString(),
                        "temporada actual" to serieUsuario.tempActual.toString(),
                        "valoracion" to serieUsuario.valoracion.toString()
                    )
                }

            return@runBlocking gsonBuilder.toJson(seriesSiguiendo)
        }
    }



}
