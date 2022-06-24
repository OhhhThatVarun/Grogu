package com.varun.grogu.domain.entities

import com.varun.grogu.data.extensions.capitalizeFirstChar
import java.io.Serializable

/**
 * Class that represents a Character in the domain layer.
 */
data class Character(
    val url: String,
    val name: String,
    val birthYear: String,
    val height: String,
    val homeWorldUrl: String,
    val species: String?,
    val filmUrls: List<String>,
) : Serializable {

    /**
     * Converts the [height] in Feet and Inches and returns with suffix.
     *
     * e.g. 5ft 4in.
     */
    fun formatHeightInFeet(): String {
        val intHeight = height.toIntOrNull() ?: return height.capitalizeFirstChar()
        val height = intHeight / 2.54
        val inch = height % 12
        val feet = height / 12
        return "${feet.toInt()}ft ${inch.toInt()}in."
    }

    /**
     * Returns the [height] with cm suffix
     *
     * e.g. 172 cm
     */
    fun formatHeightInCm(): String {
        val intHeight = height.toIntOrNull() ?: return height.capitalizeFirstChar()
        return "$intHeight cm"
    }

    fun getMovieCount(): Int {
        return filmUrls.count()
    }
}