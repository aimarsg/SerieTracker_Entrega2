package com.aimarsg.serietracker.model.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aimarsg.serietracker.model.entities.SerieUsuario
import kotlinx.coroutines.flow.Flow


/**
 * DAO defining the room database access API related to the users series data
 */
@Dao
interface SerieUsuarioDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(serieUsuario: SerieUsuario)

    @Update
    suspend fun update(serieUsuario: SerieUsuario)

    @Delete
    suspend fun delete(serieUsuario: SerieUsuario)

    @Query("SELECT * from SerieUsuario WHERE siguiendo ORDER BY titulo ASC")
    fun getSeriesUsuarioSiguiendo(): Flow<List<SerieUsuario>>

    @Query("SELECT * from SerieUsuario WHERE not siguiendo ORDER BY titulo ASC")
    fun getSeriesUsuarioPendiente(): Flow<List<SerieUsuario>>

    @Query("DELETE FROM SerieUsuario")
    suspend fun deleteAll()

    @Query("SELECT * from SerieUsuario")
    fun getAll(): List<SerieUsuario>
}