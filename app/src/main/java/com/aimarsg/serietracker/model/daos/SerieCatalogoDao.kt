package com.aimarsg.serietracker.model.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimarsg.serietracker.model.entities.SerieCatalogo
import kotlinx.coroutines.flow.Flow

/**
 * DAO defining the room database access API related to the catalogue series data
 */
@Dao
interface SerieCatalogoDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(serieCatalogo: SerieCatalogo)

    /*@Update
    suspend fun update(serieCatalogo: SerieCatalogo)*/

    @Delete
    suspend fun delete(serieCatalogo: SerieCatalogo)

    @Query("SELECT * from SerieCatalogo ORDER BY titulo ASC")
    fun getSeriesCatalogo(): Flow<List<SerieCatalogo>>

    @Query("DELETE FROM SerieCatalogo")
    suspend fun deleteAll()
}