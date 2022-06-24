package com.varun.grogu.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.varun.grogu.data.extensions.capitalizeFirstChar
import com.varun.grogu.domain.entities.Species

/**
 * Species Entity used in the data layer.
 */
data class SpeciesEntity(

    @SerializedName("name")
    val name: String,

    @SerializedName("language")
    val language: String,

    @SerializedName("url")
    val url: String
)

/**
 * Function used to transform [Species] (in the data layer) to [Species] in the domain layer.
 */
fun SpeciesEntity.toDomainModel(): Species {
    return Species(name.capitalizeFirstChar(), language.capitalizeFirstChar(), url)
}