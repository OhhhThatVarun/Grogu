package com.varun.grogu.features.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.varun.grogu.R
import com.varun.grogu.data.FakeStarWarsRepository
import com.varun.grogu.domain.entities.Character
import com.varun.grogu.domain.entities.Film
import com.varun.grogu.launchFragmentInHiltContainer
import com.varun.grogu.presentation.adapters.CharacterAdapter
import com.varun.grogu.presentation.features.detail.DetailFragment
import com.varun.grogu.presentation.features.detail.DetailFragmentDirections
import com.varun.grogu.utils.RecyclerViewItemCountAssertion
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


@ExperimentalCoroutinesApi
@HiltAndroidTest
class DetailFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val navController = Mockito.mock(NavController::class.java)

    private val character = Character(null, "test", "Luke Skywalker", "19BBY", "172", "test", null, listOf("test", "test", "test"))

    @Before
    fun setup() {
        hiltRule.inject()
        launchFragmentInHiltContainer<DetailFragment>(bundleOf("character" to character)) {
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    @Test
    fun shouldLoadCharacterDetailsFomBundle() {
        onView(withId(R.id.labelCharacterName))
            .check(matches(withText(character.name)))
        onView(withId(R.id.height))
            .check(matches(withText("${character.formatHeightInFeet()} (${character.formatHeightInCm()})")))
        onView(withId(R.id.birthYear))
            .check(matches(withText(character.birthYear)))
    }

    @Test
    fun shouldShowNoInformationForNullSpecies() {
        onView(allOf(withId(R.id.noInfoLabel), isDescendantOfA(withId(R.id.speciesDetailCard))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowPlanetDetails() {
        onView(withId(R.id.population))
            .check(matches(withText(FakeStarWarsRepository.API_RESPONSE_PLANET.getFormattedPopulation())))
        onView(withId(R.id.name))
            .check(matches(withText(FakeStarWarsRepository.API_RESPONSE_PLANET.name)))
    }

    @Test
    fun shouldShowCorrectNumberOfFilms() {
        onView(withId(R.id.filmList))
            .check(RecyclerViewItemCountAssertion(character.filmUrls.count()));
    }

    @Test
    fun testNavigationToFilmDetailFragment() {
        onView(withId(R.id.filmList))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CharacterAdapter.ViewHolder>(0, ViewActions.click()))

        Mockito.verify(navController).navigate(DetailFragmentDirections.actionDetailFragmentToFilmDetailFragment(Film("test", "Test test test", "Test")))
    }
}