package com.aimarsg.serietracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@Entity(tableName = "SerieCatalogo")
data class SerieCatalogo(
    @PrimaryKey
    val titulo: String,
    val numTemps: Int,
    val epTemp: String,
){
    override fun toString(): String {
        return "$titulo"
    }

    /*override fun equals(other: Any?): Boolean {
        when (other){
            is SerieCatalogo -> return other.titulo == this.titulo
            is SerieUsuario -> return other.titulo == this.titulo
            else -> return false
        }
    }*/
}