package com.varun.grogu.domain.entities

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class PlanetTest {

    @Test
    fun `should format numbers with comma`() {
        var planet = Planet("", "100", "")
        Assert.assertEquals("100", planet.getFormattedPopulation())

        planet = planet.copy(population = "1000")
        Assert.assertEquals("1,000", planet.getFormattedPopulation())

        planet = planet.copy(population = "10000")
        Assert.assertEquals("10,000", planet.getFormattedPopulation())

        planet = planet.copy(population = "100000")
        Assert.assertEquals("100,000", planet.getFormattedPopulation())

        planet = planet.copy(population = "1000000")
        Assert.assertEquals("1,000,000", planet.getFormattedPopulation())

        planet = planet.copy(population = "4500000000")
        Assert.assertEquals("4,500,000,000", planet.getFormattedPopulation())
    }

    @Test
    fun `should return unknown for unknown population`() {
        val planet = Planet("", "unknown", "")
        Assert.assertEquals("unknown", planet.getFormattedPopulation())
    }
}