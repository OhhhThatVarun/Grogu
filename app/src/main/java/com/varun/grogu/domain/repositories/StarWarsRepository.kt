package com.varun.grogu.domain.repositories

import com.varun.grogu.domain.entities.*
import com.varun.grogu.domain.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow


/**
 * Interface that represents a Repository for getting Star Wars related data.
 */
interface StarWarsRepository {

    /**
     * Get the [CoroutineDispatcher] which will be used by this repository when making api calls (if any)
     */
    fun getCoroutineDispatcher(): CoroutineDispatcher

    /**
     * Get a [Character] [SearchResult].
     *
     * @param query: The text used to make search on the server.
     * @param page: The page number used to retrieve user data.
     */
    suspend fun searchCharacters(query: String, page: Int? = null): SearchResult<Character>

    /**
     * Get a [Film] [Resource].
     *
     * @param url: The URL used to retrieve [Film].
     */
    suspend fun getFilm(url: String): Resource<Film>

    /**
     * Get a [Flow] which will emit a [Planet] [Resource].
     *
     * @param url: The URL used to retrieve [Planet].
     */
    suspend fun getPlanet(url: String): Flow<Resource<Planet>>

    /**
     * Get a [Flow] which will emit a [Species] [Resource].
     *
     * @param url: The URL used to retrieve [Species].
     */
    suspend fun getSpecies(url: String): Flow<Resource<Species>>
}