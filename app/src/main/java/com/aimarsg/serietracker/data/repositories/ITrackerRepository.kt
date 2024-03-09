package com.aimarsg.serietracker.data.repositories

import com.aimarsg.serietracker.data.entities.SerieUsuario
import kotlinx.coroutines.flow.Flow

interface ITrackerRepository{
    suspend fun addSerie(serieUsuario: SerieUsuario)
    suspend fun deleteSerie(serieUsuario: SerieUsuario)
    suspend fun updateSerie(serieUsuario: SerieUsuario)
    fun getSeriesSiguiendo(): Flow<List<SerieUsuario>>
    fun getSeriesPendiente(): Flow<List<SerieUsuario>>
}