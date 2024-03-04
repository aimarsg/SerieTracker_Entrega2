package com.aimarsg.serietracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.compose.runtime.Immutable

@Immutable
@Entity(tableName = "SerieCatalogo")
data class SerieCatalogo(
    @PrimaryKey
    val titulo: String,
    val numTemps: Int,
    val epTemp: String,
)