package com.varun.grogu.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.varun.grogu.data.entities.FilmEntity
import com.varun.grogu.domain.entities.Film

/**
 * Film Entity used in the data layer.
 */
data class FilmEntity(

    @SerializedName("url")
    val url: String,

    @SerializedName("opening_crawl")
    val openingCrawl: String,

    @SerializedName("title")
    val title: String,
)

/**
 * Function used to transform [FilmEntity] (in the data layer) to [Film] in the domain layer.
 */
fun FilmEntity.toDomainModel(): Film {
    return Film(url, openingCrawl, title)
}