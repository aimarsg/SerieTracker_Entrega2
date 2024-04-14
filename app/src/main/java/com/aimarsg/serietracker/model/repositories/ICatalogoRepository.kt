package com.aimarsg.serietracker.model.repositories

import com.aimarsg.serietracker.model.entities.SerieCatalogo
import kotlinx.coroutines.flow.Flow

interface ICatalogoRepository{
    fun getAllSeries(): Flow<List<SerieCatalogo>>
    suspend fun addSerie(serieCatalogo: SerieCatalogo)
    suspend fun deleteSerie(serieCatalogo: SerieCatalogo)
    suspend fun updateCatalogo()
}