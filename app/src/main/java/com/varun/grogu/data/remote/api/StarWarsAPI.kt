package com.varun.grogu.data.remote.api

import com.varun.grogu.data.remote.entities.CharacterSearchResultEntity
import com.varun.grogu.data.entities.FilmEntity
import com.varun.grogu.data.remote.entities.PlanetEntity
import com.varun.grogu.data.remote.entities.SpeciesEntity
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * HTTP StarWarsAPI for retrieving data from the network.
 */
interface StarWarsAPI {

    @GET("people/")
    suspend fun searchCharacters(@Query("search") query: String, @Query("page") page: Int?): CharacterSearchResultEntity

    @GET
    suspend fun getFilm(@Url url: String): FilmEntity

    @GET
    suspend fun getPlanet(@Url url: String): PlanetEntity

    @GET
    suspend fun getSpecies(@Url url: String): SpeciesEntity
}