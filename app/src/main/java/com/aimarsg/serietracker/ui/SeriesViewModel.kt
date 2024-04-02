package com.aimarsg.serietracker.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimarsg.serietracker.model.Idioma
import com.aimarsg.serietracker.model.MyPreferencesDataStore
import com.aimarsg.serietracker.model.entities.SerieCatalogo
import com.aimarsg.serietracker.model.entities.SerieUsuario
import com.aimarsg.serietracker.model.repositories.CatalogoRepository
import com.aimarsg.serietracker.model.repositories.TrackerRepository
import com.aimarsg.serietracker.model.webclient.APIClient
import com.aimarsg.serietracker.model.webclient.AuthenticationException
import com.aimarsg.serietracker.model.webclient.UserExistsException
import com.aimarsg.serietracker.utils.CambioDeIdioma
import com.aimarsg.serietracker.utils.today
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import java.io.File
import javax.inject.Inject


/**
 * Apps main viewmodel, used to store variables temporarily and
 * exchange information with the database, following the model-view-viewmodel (MVVM) architecture
 * Hilt is used to inject the dependencies on the constructor
 */
@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val myPreferencesDataStore: MyPreferencesDataStore,
    private val catalogoRepository: CatalogoRepository,
    private val trackerRepository: TrackerRepository,
    private val cambioDeIdioma: CambioDeIdioma,
    private val ApiClient: APIClient // Added the API client to the viewmodel for the authentication and data retrieve
): ViewModel() {

    // get the theme and language from the data store
    val tema = myPreferencesDataStore.preferencesStatusFlow.map {
        it.temaClaro
    }
    val idioma = myPreferencesDataStore.preferencesStatusFlow.map {
        it.idioma
    }

    // selected language
    val idiomaActual by cambioDeIdioma::idiomaActual

    // series lists
    val seriesCatalogo = catalogoRepository.getAllSeries()
    val seriesSiguiendo = trackerRepository.getSeriesSiguiendo()
    val seriesPendiente = trackerRepository.getSeriesPendiente()

    // variable to store the selected serie
    var serieSeleccionada by mutableStateOf(SerieCatalogo("", 0, ""))

    // variable to store the selected date
    var selectedDate by mutableStateOf(LocalDate.today.toString())

    // variable to store the authenticated users token
    var token by mutableStateOf("")

    /**
     * Update the selected theme on the preferences data store
     * @param theme false if dark, true else
     */
    fun updateTheme(theme: Boolean){
        viewModelScope.launch {
            myPreferencesDataStore.updateTheme(theme)
        }
    }


    /**
     * Update the selected language on the preferences data store and update the app language
     * @param idioma new language
     */
    fun updateIdioma(idioma: Idioma, context: Context){
        cambioDeIdioma.cambiarIdioma(idioma, context)
        viewModelScope.launch {
            myPreferencesDataStore.updateIdioma(idioma)
        }
    }

    fun reloadLang(idioma: Idioma, context: Context) = cambioDeIdioma.cambiarIdioma(idioma, context, false)

    // functions to add / edit / modify users series
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


    /**
     * function to get the user's series serialized as JSON to export all data
      */
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

    /**
     * Function to authenticate user
     * @param user: user name
     * @param password: user password
     * @return true if the user is authenticated, false else
     */
    @Throws(AuthenticationException::class, Exception::class)
    suspend fun authenticate(user: String, password: String){
        ApiClient.authenticate(user, password)
    }

    /**
     * Function to register a new user
     * @param user: user name
     * @param password: user password
     * @return true if the user is registered, false else
     */
    @Throws(UserExistsException::class, Exception::class)
    suspend fun register(user: String, password: String) {
        ApiClient.register(user, password)
    }

    // Methos to manage profile picture
    fun getProfilePicture(callback: (Bitmap?) -> Unit) {
        viewModelScope.launch {
            val bitmap = ApiClient.getFotoDePerfil()
            callback(bitmap)
        }
    }

    fun subirFotoDePerfil(image: File){
        viewModelScope.launch {
            ApiClient.subirFotoDePerfil(image)
        }
    }

}
