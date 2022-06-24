package com.varun.grogu.features.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.varun.grogu.R
import com.varun.grogu.data.FakeStarWarsRepository
import com.varun.grogu.launchFragmentInHiltContainer
import com.varun.grogu.presentation.adapters.CharacterAdapter
import com.varun.grogu.presentation.features.home.HomeFragment
import com.varun.grogu.presentation.features.home.HomeFragmentDirections
import com.varun.grogu.utils.RecyclerViewItemCountAssertion
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@ExperimentalCoroutinesApi
@HiltAndroidTest
class HomeFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val navController = mock(NavController::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
        launchFragmentInHiltContainer<HomeFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    @Test
    fun testTypingInvokingSearch() {
        onView(withId(R.id.search))
            .perform(typeText("Luke Skywalker"))
            .perform(pressImeActionButton())

        /**
         * Very very bad idea. Should use espresso idling resource
         */
        Thread.sleep(1500)

        onView(withId(R.id.characterList))
            .check(RecyclerViewItemCountAssertion(FakeStarWarsRepository.API_RESPONSE_SEARCH.results.count()));
    }

    @Test
    fun testNavigationToDetailFragment() {
        onView(withId(R.id.search))
            .perform(typeText("Luke Skywalker"))
            .perform(pressImeActionButton())

        /**
         * Very very bad idea. Should use espresso idling resource
         */
        Thread.sleep(1500)

        onView(withId(R.id.characterList))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CharacterAdapter.ViewHolder>(0, click()))

        verify(navController).navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(FakeStarWarsRepository.API_RESPONSE_SEARCH.results[0]))
    }
}