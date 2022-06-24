package com.varun.grogu.features.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.varun.grogu.R
import com.varun.grogu.domain.entities.Film
import com.varun.grogu.launchFragmentInHiltContainer
import com.varun.grogu.presentation.features.detail.FilmDetailFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
@HiltAndroidTest
class FilmDetailFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val film = Film("", "Test film description!", "Test Film")

    @Before
    fun setup() {
        hiltRule.inject()
        launchFragmentInHiltContainer<FilmDetailFragment>(bundleOf("film" to film))
    }

    @Test
    fun shouldShowFilmDetailsFomBundle() {
        onView(ViewMatchers.withId(R.id.filmTitle))
            .check(ViewAssertions.matches(ViewMatchers.withText(film.title)))
        onView(ViewMatchers.withId(R.id.filmDescription))
            .check(ViewAssertions.matches(ViewMatchers.withText(film.fullDescription)))
    }
}