package com.aimarsg.serietracker.model.repositories

import com.aimarsg.serietracker.model.daos.SerieCatalogoDao
import com.aimarsg.serietracker.model.entities.SerieCatalogo
import com.aimarsg.serietracker.model.webclient.APIClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This repository makes use of the serieCatlogo DAO, which is injected by hilt here
 * It implements the CRUD operations defined in the interface using the dao methods
 */

@Singleton
class CatalogoRepository @Inject constructor(
    private val serieCatalogoDao: SerieCatalogoDao,
    private val apiClient: APIClient
): ICatalogoRepository{
    override fun getAllSeries(): Flow<List<SerieCatalogo>> = serieCatalogoDao.getSeriesCatalogo()

    override suspend fun addSerie(serieCatalogo: SerieCatalogo) = serieCatalogoDao.insert(serieCatalogo)

    override suspend fun deleteSerie(serieCatalogo: SerieCatalogo) = serieCatalogoDao.delete(serieCatalogo)

    @Throws(Exception::class)
    override suspend fun updateCatalogo() {
        // Delete all the series and download the new ones
        serieCatalogoDao.deleteAll()
        val seriesCatalogo = apiClient.downloadCatalogue()
        for (serie in seriesCatalogo){
            serieCatalogoDao.insert(serie)
        }
    }
}