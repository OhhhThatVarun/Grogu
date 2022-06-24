package com.varun.grogu.data

import com.varun.grogu.domain.entities.*
import com.varun.grogu.domain.repositories.StarWarsRepository
import com.varun.grogu.domain.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeStarWarsRepository : StarWarsRepository {

    override fun getCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override suspend fun getFavCharacters(): Resource<List<Character>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchCharacters(query: String, page: Int?): SearchResult<Character> {
        return API_RESPONSE_SEARCH
    }

    override suspend fun getFilm(url: String): Resource<Film> {
        return Resource.Success(Film("test", "Test test test", "Test"))
    }

    override suspend fun getPlanet(url: String): Flow<Resource<Planet>> {
        return flow {
            emit(Resource.Success(API_RESPONSE_PLANET))
        }
    }

    override suspend fun getSpecies(url: String): Flow<Resource<Species>> {
        TODO("Not yet implemented")
    }

    companion object {

        val API_RESPONSE_PLANET = Planet("https://swapi.dev/api/planets/1/", "200000", "Tatooine")

        val API_RESPONSE_SEARCH = SearchResult(
            listOf(
                Character(
                    null,
                    "https://swapi.dev/api/people/1/", "Luke Skywalker", "19BBY", "172", "https://swapi.dev/api/planets/1/", null,
                    listOf(
                        "https://swapi.dev/api/films/1/",
                        "https://swapi.dev/api/films/2/",
                        "https://swapi.dev/api/films/3/",
                        "https://swapi.dev/api/films/6/"
                    )
                )
            ), null, null
        )
    }
}