package com.aimarsg.serietracker.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.myPreferencesDataStore : DataStore<Preferences> by preferencesDataStore("settings")

enum class Idioma(val codigo:String){
    Castellano(codigo = "es"),
    English(codigo = "en"),
    Euskera(codigo = "eu")
}

data class Settings(
    val temaClaro : Boolean,
    val idioma: Idioma
)


//LO DE AQUI ABAJO ES EQUIVALENTE AL REPOSITORIO
@Singleton
class MyPreferencesDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val myPreferencesDataStore : DataStore<Preferences> = context.myPreferencesDataStore
    private object PreferencesKeys{
        val IDIOMA_SELECCIONADO = stringPreferencesKey("idioma")
        val TEMA_CLARO_SELECCIONADO = booleanPreferencesKey("tema")
    }
    val preferencesStatusFlow = myPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {preferences ->
            val tema = preferences[PreferencesKeys.TEMA_CLARO_SELECCIONADO]?: true
            val idioma =
                Idioma.valueOf(
                    preferences[PreferencesKeys.IDIOMA_SELECCIONADO]?: Idioma.Castellano.name
                )
            Settings(tema, idioma)
        }

    suspend fun updateTheme(temaclaro: Boolean){
        myPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.TEMA_CLARO_SELECCIONADO] = temaclaro
        }
    }
    suspend fun updateIdioma(idioma: Idioma){
        myPreferencesDataStore.edit{preferences ->
            preferences[PreferencesKeys.IDIOMA_SELECCIONADO] = idioma.name
        }
    }
}

