package com.varun.grogu.domain.entities

import java.text.NumberFormat
import java.util.*

/**
 * Class that represents a Planet in the domain layer.
 */
data class Planet(
    val url: String,
    val population: String,
    val name: String,
) {

    /**
     * Returns the [population] with commas
     */
    fun getFormattedPopulation(): String {
        val intPopulation = population.toLongOrNull() ?: return population
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(intPopulation)
    }
}