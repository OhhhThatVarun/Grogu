package com.varun.grogu.presentation.features

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.varun.grogu.data.remote.api.StarWarsAPI
import com.varun.grogu.data.remote.entities.CharacterEntity
import com.varun.grogu.data.remote.entities.CharacterSearchResultEntity
import com.varun.grogu.data.repositories.StarWarsRepositoryImpl
import com.varun.grogu.presentation.extensions.getOrAwaitValue
import com.varun.grogu.presentation.features.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
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
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutionRule = InstantTaskExecutorRule()

    private lateinit var homeViewModel: HomeViewModel
    private val starWarsAPI = mock(StarWarsAPI::class.java)
    private val testDispatcher = UnconfinedTestDispatcher() // Stan

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        homeViewModel = HomeViewModel(StarWarsRepositoryImpl(starWarsAPI, testDispatcher))
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `live data should emit failure when api throws an error`() = runTest {
        val searchQuery = "testQuery"
        val error = RuntimeException("500", Throwable())
        `when`(starWarsAPI.searchCharacters(searchQuery, null)).thenThrow(error)
        homeViewModel.searchQuery.value = searchQuery

        homeViewModel.retrySearch()
        val result = homeViewModel.characters.getOrAwaitValue()

        assertEquals(PagingSource.LoadResult.Page(listOf(), null, null), result)
    }

    @Test
    fun `live data should emit success when retying search`() = runTest {
        val character = CharacterEntity("", "", "", "172", "", listOf(), listOf())
        val searchQuery = "testQuery"
        `when`(starWarsAPI.searchCharacters(searchQuery, null)).thenReturn(CharacterSearchResultEntity(listOf(character, character), null, null))

        homeViewModel.searchQuery.value = searchQuery
        val result = homeViewModel.characters.getOrAwaitValue()

        homeViewModel.retrySearch()
        val retryResult = homeViewModel.characters.getOrAwaitValue()

        assertEquals(retryResult, result)
    }

    @Test
    fun `live data should emit success with result when searching for a character`() = runTest {
        val character = CharacterEntity("", "", "", "172", "", listOf(), listOf())
        `when`(starWarsAPI.searchCharacters("a", null)).thenReturn(CharacterSearchResultEntity(listOf(character, character), null, null))

        homeViewModel.searchQuery.value = "a"
        val result = homeViewModel.characters.getOrAwaitValue()

        assertEquals(PagingSource.LoadResult.Page(listOf(), null, null), result)
    }

//    @Test
//    fun `should not call repository when searching with empty string`() = runTest {
//
//    }
}