package com.aimarsg.serietracker.data.repositories

import com.aimarsg.serietracker.data.daos.SerieCatalogoDao
import com.aimarsg.serietracker.data.entities.SerieCatalogo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CatalogoRepository @Inject constructor(
    private val serieCatalogoDao: SerieCatalogoDao
): ICatalogoRepository{
    override fun getAllSeries(): Flow<List<SerieCatalogo>> = serieCatalogoDao.getSeriesCatalogo()

    override suspend fun addSerie(serieCatalogo: SerieCatalogo) = serieCatalogoDao.insert(serieCatalogo)

    override suspend fun deleteSerie(serieCatalogo: SerieCatalogo) = serieCatalogoDao.delete(serieCatalogo)
}