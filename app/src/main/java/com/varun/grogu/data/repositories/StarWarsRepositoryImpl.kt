package com.varun.grogu.data.repositories

import com.varun.grogu.data.remote.api.StarWarsAPI
import com.varun.grogu.data.remote.entities.toDomainModel
import com.varun.grogu.data.utils.safeApiCallWithCoroutine
import com.varun.grogu.data.utils.safeApiCallWithFlow
import com.varun.grogu.domain.repositories.StarWarsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of [StarWarsRepository] for retrieving data from the star-wars Rest API.
 *
 * @param starWarsAPI: API for retrieving data from the network.
 * @param coroutineDispatcher: A coroutineDispatcher used to make network calls
 */
class StarWarsRepositoryImpl(private val starWarsAPI: StarWarsAPI, private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) : StarWarsRepository {

    override fun getCoroutineDispatcher() = coroutineDispatcher

    override suspend fun searchCharacters(query: String, page: Int?) = withContext(coroutineDispatcher) {
        starWarsAPI.searchCharacters(query, page).toDomainModel()
    }

    override suspend fun getFilm(url: String) = safeApiCallWithCoroutine(coroutineDispatcher) {
        starWarsAPI.getFilm(url).toDomainModel()
    }

    override suspend fun getPlanet(url: String) = safeApiCallWithFlow(coroutineDispatcher) {
        starWarsAPI.getPlanet(url).toDomainModel()
    }

    override suspend fun getSpecies(url: String) = safeApiCallWithFlow(coroutineDispatcher) {
        starWarsAPI.getSpecies(url).toDomainModel()
    }
}