package com.aimarsg.serietracker.model.repositories

import com.aimarsg.serietracker.model.daos.SerieUsuarioDao
import com.aimarsg.serietracker.model.entities.SerieUsuario
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This repository makes use of the SerieUsuarioDao DAO, which is injected by hilt here
 * It implements the CRUD operations defined in the interface using the dao methods
 */

@Singleton
class TrackerRepository @Inject constructor(
    private val serieUsuarioDao: SerieUsuarioDao
): ITrackerRepository{
    override suspend fun addSerie(serieUsuario: SerieUsuario) = serieUsuarioDao.insert(serieUsuario)

    override suspend fun deleteSerie(serieUsuario: SerieUsuario) = serieUsuarioDao.delete(serieUsuario)

    override suspend fun updateSerie(serieUsuario: SerieUsuario) = serieUsuarioDao.update(serieUsuario)

    override fun getSeriesPendiente(): Flow<List<SerieUsuario>> = serieUsuarioDao.getSeriesUsuarioPendiente()

    override fun getSeriesSiguiendo(): Flow<List<SerieUsuario>> = serieUsuarioDao.getSeriesUsuarioSiguiendo()
}
