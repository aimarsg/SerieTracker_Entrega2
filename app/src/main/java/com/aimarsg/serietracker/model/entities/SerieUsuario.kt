package com.aimarsg.serietracker.model.entities

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@Entity(tableName = "SerieUsuario")
data class SerieUsuario(
    //@PrimaryKey
    //val id: Int = 0,

    //datos de la serie como tal
    @PrimaryKey
    val titulo: String,
    val numTemps: Int,
    val epTemp: String,

    //datos del usuario
    val siguiendo: Boolean,
    val recordatorio: LocalDate,
    val epActual: Int,
    val tempActual: Int,
    val valoracion: Float
){
    /*override fun equals(other: Any?): Boolean {
        when (other){
            is SerieCatalogo -> return other.titulo == this.titulo
            is SerieUsuario -> return other.titulo == this.titulo
            else -> return false
        }
    }*/
}