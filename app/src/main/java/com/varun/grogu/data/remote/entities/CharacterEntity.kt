package com.varun.grogu.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.varun.grogu.data.extensions.capitalizeFirstChar
import com.varun.grogu.domain.entities.Character
import java.io.Serializable

/**
 * Character Entity used in the data layer.
 */
data class CharacterEntity(

    @SerializedName("url")
    val url: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("birth_year")
    val birthYear: String,

    @SerializedName("height")
    val height: String,

    @SerializedName("homeworld")
    val homeWorld: String,

    @SerializedName("species")
    val species: List<String>,

    @SerializedName("films")
    val films: List<String>

) : Serializable

/**
 * Function used to transform [CharacterEntity] (in the data layer) to [Character] in the domain layer.
 */
fun CharacterEntity.toDomainModel(): Character {
    return Character(url, name.capitalizeFirstChar(), birthYear.capitalizeFirstChar(), height, homeWorld, species.getOrNull(0), films)
}