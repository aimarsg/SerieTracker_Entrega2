package com.aimarsg.serietracker.data.repositories

import com.aimarsg.serietracker.data.daos.SerieCatalogoDao
import com.aimarsg.serietracker.data.daos.SerieUsuarioDao
import com.aimarsg.serietracker.data.entities.SerieCatalogo
import com.aimarsg.serietracker.data.entities.SerieUsuario
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface ITrackerRepository{
    suspend fun addSerie(serieUsuario: SerieUsuario)
    suspend fun deleteSerie(serieUsuario: SerieUsuario)
    suspend fun updateSerie(serieUsuario: SerieUsuario)
    fun getSeriesSiguiendo(): Flow<List<SerieUsuario>>
    fun getSeriesPendiente(): Flow<List<SerieUsuario>>
}

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
