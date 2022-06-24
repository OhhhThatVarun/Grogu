package com.varun.grogu.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.varun.grogu.domain.entities.Character
import com.varun.grogu.domain.entities.SearchResult

/**
 * CharacterSearchResult Entity used in the data layer.
 */
data class CharacterSearchResultEntity(

    @SerializedName("results")
    val characters: List<CharacterEntity>,

    @SerializedName("previous")
    val previous: String?,

    @SerializedName("next")
    val next: String?
)

/**
 * Function used to transform [CharacterSearchResultEntity] (in the data layer) to [SearchResult] of [Character] in the domain layer.
 */
fun CharacterSearchResultEntity.toDomainModel(): SearchResult<Character> {
    return SearchResult(characters.map { it.toDomainModel() }, previous, next)
}