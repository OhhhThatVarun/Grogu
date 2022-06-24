package com.varun.grogu.presentation.paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.varun.grogu.data.remote.api.StarWarsAPI
import com.varun.grogu.data.remote.entities.CharacterSearchResultEntity
import com.varun.grogu.data.entities.toDomainModel
import com.varun.grogu.data.repositories.StarWarsRepositoryImpl
import com.varun.grogu.domain.entities.Character
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
import org.mockito.BDDMockito.`when`
import org.mockito.Mockito.mock
import org.testng.annotations.AfterTest


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CharacterSearchPagingSourceTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val starWarsAPI = mock(StarWarsAPI::class.java)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return empty results when api return empty results`() = runTest {
        val searchQuery = "aaa"
        `when`(starWarsAPI.searchCharacters(searchQuery, null)).thenReturn(CharacterSearchResultEntity(listOf(), null, null))
        val expectedResult = PagingSource.LoadResult.Page<Int, Character>(listOf(), null, null)
        val characterSearchPagingSource = CharacterSearchPagingSource(StarWarsRepositoryImpl(starWarsAPI, testDispatcher), searchQuery)

        val actualResult = characterSearchPagingSource.load(PagingSource.LoadParams.Refresh(key = null, loadSize = 2, placeholdersEnabled = false))

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `should return results when api return results`() = runTest {
        val searchQuery = "aaa"
        val characters = mutableListOf<com.varun.grogu.data.remote.entities.CharacterEntity>().apply {
            repeat(20) {
                add(com.varun.grogu.data.remote.entities.CharacterEntity("Page1 $it", "Page1 $it", "Page1 $it", "Page1 $it", "Page1 $it", listOf(), listOf()))
            }
        }.toList()
        `when`(starWarsAPI.searchCharacters(searchQuery, null)).thenReturn(CharacterSearchResultEntity(characters, null, null))
        val expectedResult = PagingSource.LoadResult.Page<Int, Character>(characters.map { it.toDomainModel() }, null, null)
        val characterSearchPagingSource = CharacterSearchPagingSource(StarWarsRepositoryImpl(starWarsAPI, testDispatcher), searchQuery)

        val actualResult = characterSearchPagingSource.load(PagingSource.LoadParams.Refresh(key = null, loadSize = 2, placeholdersEnabled = false))

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `should return error result when api throws an error`() = runTest {
        val searchQuery = "aaa"
        val error = RuntimeException("test error")
        `when`(starWarsAPI.searchCharacters(searchQuery, null)).thenThrow(error)
        val expectedResult = PagingSource.LoadResult.Error<Int, Character>(error).throwable
        val characterSearchPagingSource = CharacterSearchPagingSource(StarWarsRepositoryImpl(starWarsAPI, testDispatcher), searchQuery)

        val actualResult = characterSearchPagingSource.load(PagingSource.LoadParams.Refresh(key = null, loadSize = 2, placeholdersEnabled = false))

        assertTrue(actualResult is PagingSource.LoadResult.Error)
        assertEquals(expectedResult.message, (actualResult as PagingSource.LoadResult.Error).throwable.message)
    }

    @Test
    fun `should append results to the current list`() = runTest {
        val searchQuery = "aaa"
        val page1Characters = mutableListOf<com.varun.grogu.data.remote.entities.CharacterEntity>().apply {
            repeat(5) {
                add(com.varun.grogu.data.remote.entities.CharacterEntity("Page1 $it", "Page1 $it", "Page1 $it", "Page1 $it", "Page1 $it", listOf(), listOf()))
            }
        }.toList()
        `when`(starWarsAPI.searchCharacters(searchQuery, 1)).thenReturn(CharacterSearchResultEntity(page1Characters, null, "https://swapi.dev/api/people/?page=2"))
        val expectedResult = PagingSource.LoadResult.Page(data = page1Characters.map { it.toDomainModel() }, prevKey = null, nextKey = null)
        val characterSearchPagingSource = CharacterSearchPagingSource(StarWarsRepositoryImpl(starWarsAPI, testDispatcher), searchQuery)

        val actualResult = characterSearchPagingSource.load(PagingSource.LoadParams.Append(key = 1, loadSize = 50, placeholdersEnabled = false))

        assertEquals(expectedResult, actualResult)
    }
}