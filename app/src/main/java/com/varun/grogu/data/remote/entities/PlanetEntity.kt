package com.varun.grogu.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.varun.grogu.data.extensions.capitalizeFirstChar
import com.varun.grogu.domain.entities.Planet

/**
 * Planet Entity used in the data layer.
 */
data class PlanetEntity(

    @SerializedName("url")
    val url: String,

    @SerializedName("population")
    val population: String,

    @SerializedName("name")
    val name: String,
)

/**
 * Function used to transform [PlanetEntity] (in the data layer) to [Planet] in the domain layer.
 */
fun PlanetEntity.toDomainModel(): Planet {
    return Planet(url, population.capitalizeFirstChar(), name.capitalizeFirstChar())
}