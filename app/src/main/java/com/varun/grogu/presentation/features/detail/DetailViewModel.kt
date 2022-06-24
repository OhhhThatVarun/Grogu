package com.varun.grogu.presentation.features.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varun.grogu.domain.entities.Character
import com.varun.grogu.domain.entities.Film
import com.varun.grogu.domain.entities.Planet
import com.varun.grogu.domain.entities.Species
import com.varun.grogu.domain.repositories.StarWarsRepository
import com.varun.grogu.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] to handle state of [DetailFragment]
 *
 * @param starWarsRepository: Repository used for retrieving data.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(private val starWarsRepository: StarWarsRepository) : ViewModel() {

    /**
     * Exposed [LiveData] that emits [List] of [Film] [Resource]
     */
    val films: LiveData<List<Resource<Film>>> get() = _films
    private val _films = MutableLiveData<List<Resource<Film>>>()

    /**
     * Exposed [LiveData] that emits [Planet] [Resource]
     */
    val planet: LiveData<Resource<Planet>> get() = _planet
    private val _planet = MutableLiveData<Resource<Planet>>()

    /**
     * Exposed [LiveData] that emits [Species] [Resource]
     */
    val species: LiveData<Resource<Species>> get() = _species
    private val _species = MutableLiveData<Resource<Species>>()

    /**
     * Loads the Home [Planet] of a [Character]
     *
     * @param character: The [Character] used to retrieve [Planet]
     */
    fun loadHomePlanet(character: Character) {
        viewModelScope.launch {
            starWarsRepository.getPlanet(character.homeWorldUrl).collectLatest {
                _planet.postValue(it)
            }
        }
    }

    /**
     * Loads all the [Film] of a [Character]
     *
     * @param character: [Character] used to retrieve [Film]
     */
    fun loadFilms(character: Character) {
        val films = mutableListOf<Deferred<Resource<Film>>>()
        viewModelScope.launch {
            for (filmUrl in character.filmUrls) {
                val film = async { getFilmDetails(filmUrl) }
                film.let { films.add(it) }
            }
            films.map { it.await() }
            _films.postValue(films.awaitAll())
        }
    }

    /**
     * Loads the [Species] of a [Character]
     *
     * @param character: The [Character] used to retrieve [Species]
     */
    fun loadSpecies(character: Character) {
        if (character.species == null) return
        viewModelScope.launch {
            starWarsRepository.getSpecies(character.species).collectLatest {
                _species.postValue(it)
            }
        }
    }

    private suspend fun getFilmDetails(filmUrl: String): Resource<Film> {
        return starWarsRepository.getFilm(filmUrl)
    }
}