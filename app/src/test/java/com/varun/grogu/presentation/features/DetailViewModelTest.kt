package com.varun.grogu.presentation.features

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.varun.grogu.data.local.dao.CharacterDao
import com.varun.grogu.data.remote.api.StarWarsAPI
import com.varun.grogu.data.entities.FilmEntity
import com.varun.grogu.data.remote.entities.PlanetEntity
import com.varun.grogu.data.remote.entities.SpeciesEntity
import com.varun.grogu.data.entities.toDomainModel
import com.varun.grogu.data.repositories.StarWarsRepositoryImpl
import com.varun.grogu.domain.entities.Character
import com.varun.grogu.domain.utils.Resource
import com.varun.grogu.presentation.extensions.getOrAwaitValue
import com.varun.grogu.presentation.features.detail.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.testng.annotations.AfterTest

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DetailViewModelTest {

    @get:Rule
    val instantTaskExecutionRule = InstantTaskExecutorRule()

    private lateinit var detailViewModel: DetailViewModel
    private val starWarsAPI = mock(StarWarsAPI::class.java)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        detailViewModel = DetailViewModel(StarWarsRepositoryImpl(starWarsAPI, mock(CharacterDao::class.java), testDispatcher))
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `live data should emit success when loading a planet`() {
        runTest {
            val planetUrl = "https://swapi.dev/api/planets/1/"
            val character = mock(Character::class.java).apply {
                `when`(homeWorldUrl).thenReturn(planetUrl)
            }
            val expectedPlanet = PlanetEntity(planetUrl, "200000", "Tatooine").apply {
                `when`(starWarsAPI.getPlanet(planetUrl)).thenReturn(this)
            }

            detailViewModel.loadHomePlanet(character)
            val planetResource = detailViewModel.planet.getOrAwaitValue()

            assertTrue(planetResource is Resource.Success)
            assertEquals(expectedPlanet.toDomainModel(), (planetResource as Resource.Success).data)
        }
    }

    @Test
    fun `live data should emit failure when loading a planet if api throws an error`() {
        runTest {
            val planetUrl = "https://swapi.dev/api/planets/1/"
            val character = mock(Character::class.java).apply {
                `when`(homeWorldUrl).thenReturn(planetUrl)
            }
            val throwable = RuntimeException("test exception")
            `when`(starWarsAPI.getPlanet(planetUrl)).thenThrow(throwable)

            detailViewModel.loadHomePlanet(character)
            val planetResource = detailViewModel.planet.getOrAwaitValue()

            assertTrue(planetResource is Resource.Failure)
            assertEquals(throwable.message, (planetResource as Resource.Failure).throwable.message)
        }
    }

    @Test
    fun `live data should emit success when loading a species`() {
        runTest {
            val speciesUrl = "https://swapi.dev/api/species/1/"
            val character = mock(Character::class.java).apply {
                `when`(species).thenReturn(speciesUrl)
            }
            val expectedSpeciesEntity = SpeciesEntity("Martian", "English", speciesUrl).apply {
                `when`(starWarsAPI.getSpecies(speciesUrl)).thenReturn(this)
            }

            detailViewModel.loadSpecies(character)
            val speciesResource = detailViewModel.species.getOrAwaitValue()

            assertTrue(speciesResource is Resource.Success)
            assertEquals(expectedSpeciesEntity.toDomainModel(), (speciesResource as Resource.Success).data)
        }
    }

    @Test
    fun `live data should emit failure when loading a species if api throws an error`() {
        runTest {
            val speciesUrl = "https://swapi.dev/api/species/1/"
            val character = mock(Character::class.java).apply {
                `when`(species).thenReturn(speciesUrl)
            }
            val throwable = RuntimeException("test exception")
            `when`(starWarsAPI.getSpecies(speciesUrl)).thenThrow(throwable)

            detailViewModel.loadSpecies(character)
            val speciesResource = detailViewModel.species.getOrAwaitValue()

            assertTrue(speciesResource is Resource.Failure)
            assertEquals(throwable.message, (speciesResource as Resource.Failure).throwable.message)
        }
    }

    @Test
    fun `live data should emit success when loading films`() {
        runTest {
            val filmUrl = "https://swapi.dev/api/film/1/"
            val character = mock(Character::class.java).apply {
                `when`(filmUrls).thenReturn(listOf(filmUrl))
            }
            val expectedFilm = FilmEntity(filmUrl, "Test description", "Test film").apply {
                `when`(starWarsAPI.getFilm(filmUrl)).thenReturn(this)
            }

            detailViewModel.loadFilms(character)
            val filmsResource = detailViewModel.films.getOrAwaitValue()

            assertTrue(filmsResource.size == 1)
            assertTrue(filmsResource[0] is Resource.Success)
            assertEquals(com.varun.grogu.data.entities.toDomainModel(), (filmsResource[0] as Resource.Success).data)
        }
    }

    @Test
    fun `live data should emit failure when loading films if api throws an error`() {
        runTest {
            val filmUrl = "https://swapi.dev/api/film/1/"
            val character = mock(Character::class.java).apply {
                `when`(filmUrls).thenReturn(listOf(filmUrl))
            }
            val throwable = RuntimeException("test exception")
            `when`(starWarsAPI.getFilm(filmUrl)).thenThrow(throwable)

            detailViewModel.loadFilms(character)
            val filmsResource = detailViewModel.films.getOrAwaitValue()

            assertTrue(filmsResource[0] is Resource.Failure)
            assertEquals(throwable.message, (filmsResource[0] as Resource.Failure).throwable.message)
        }
    }
}