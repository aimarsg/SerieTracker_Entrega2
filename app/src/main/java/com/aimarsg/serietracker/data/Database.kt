package com.aimarsg.serietracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.aimarsg.serietracker.data.daos.SerieCatalogoDao
import com.aimarsg.serietracker.data.daos.SerieUsuarioDao
import com.aimarsg.serietracker.data.entities.SerieCatalogo
import com.aimarsg.serietracker.data.entities.SerieUsuario
import com.aimarsg.serietracker.utils.epochSecond
import com.aimarsg.serietracker.utils.epochSeconds
import com.aimarsg.serietracker.utils.fromEpochSeconds
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Database(
    entities = [SerieCatalogo::class, SerieUsuario::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase(){
    abstract fun serieUsuarioDao(): SerieUsuarioDao
    abstract fun serieCatalogoDao(): SerieCatalogoDao
}

class Converters {
    //--------   ZonedDateTime Converters   --------//

    // They convert from ZonedDateTime to long format and backwards. Time zone value is kept.

    @TypeConverter
    fun fromLongToDate(value: Long): LocalDate = LocalDate.fromEpochSeconds(value)


    @TypeConverter
    fun fromLongToDatetime(value: Long): LocalDateTime = LocalDateTime.fromEpochSeconds(value)


    @TypeConverter
    fun dateToTimestamp(date: LocalDate): Long = date.epochSeconds


    @TypeConverter
    fun datetimeToTimestamp(date: LocalDateTime): Long = date.epochSecond


}