package com.varun.grogu.domain.entities

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CharacterTest {

    @Test
    fun `should convert cm height to feet and inches`() {
        var character = Character("", "", "", "172", "", "", listOf())
        Assert.assertEquals("5ft 7in.", character.formatHeightInFeet())

        character = character.copy(height = "189")
        Assert.assertEquals("6ft 2in.", character.formatHeightInFeet())

        character = character.copy(height = "195")
        Assert.assertEquals("6ft 4in.", character.formatHeightInFeet())

        character = character.copy(height = "0")
        Assert.assertEquals("0ft 0in.", character.formatHeightInFeet())
    }

    @Test
    fun `should return correct number of movie count`() {
        val character = Character("", "", "", "0", "", "", listOf("1", "2", "3"))
        Assert.assertEquals(3, character.getMovieCount())
    }

    @Test
    fun `should format cm height with cm prefix`() {
        val character = Character("", "", "", "189", "", "", listOf())
        Assert.assertEquals("189 cm", character.formatHeightInCm())
    }

    @Test
    fun `should return unknown for unknown height`() {
        val character = Character("", "", "", "unknown", "", "", listOf())
        Assert.assertEquals("Unknown", character.formatHeightInFeet())
        Assert.assertEquals("Unknown", character.formatHeightInCm())
    }
}