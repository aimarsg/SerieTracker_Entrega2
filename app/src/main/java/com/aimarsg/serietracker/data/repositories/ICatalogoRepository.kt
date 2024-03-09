package com.aimarsg.serietracker.data.repositories

import com.aimarsg.serietracker.data.entities.SerieCatalogo
import kotlinx.coroutines.flow.Flow

interface ICatalogoRepository{
    fun getAllSeries(): Flow<List<SerieCatalogo>>
    suspend fun addSerie(serieCatalogo: SerieCatalogo)
    suspend fun deleteSerie(serieCatalogo: SerieCatalogo)
}