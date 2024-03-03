package com.aimarsg.serietracker.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimarsg.serietracker.data.Idioma
import com.aimarsg.serietracker.data.MyPreferencesDataStore
import com.aimarsg.serietracker.utils.CambioDeIdioma
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val myPreferencesDataStore: MyPreferencesDataStore,
    private val cambioDeIdioma: CambioDeIdioma
): ViewModel() {
    val tema = myPreferencesDataStore.preferencesStatusFlow.map {
        it.temaClaro
    }
    val idioma = myPreferencesDataStore.preferencesStatusFlow.map {
        it.idioma
    }

    val idiomaActual by cambioDeIdioma::idiomaActual

    var tituloInput by mutableStateOf("")

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



}
