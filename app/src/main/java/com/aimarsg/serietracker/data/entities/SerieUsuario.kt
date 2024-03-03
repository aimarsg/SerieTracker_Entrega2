package com.aimarsg.serietracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "SerieUsuario")
data class SerieUsuario(
    //datos de la serie como tal
    @PrimaryKey
    val titulo: String,
    val numTemps: Int,
    val epTemp: String,

    //datos del usuario
    val siguiendo: Boolean,
    val recordatorio: Date,
    val epActual: Int,
    val tempActual: Int,
    val valoracion: Float
)