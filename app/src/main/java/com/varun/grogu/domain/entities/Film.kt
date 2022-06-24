package com.varun.grogu.domain.entities

import java.io.Serializable

/**
 * Class that represents a Film in the domain layer.
 */
data class Film(
    val url: String,
    val fullDescription: String,
    val title: String
) : Serializable {

    fun getDescriptionFirstPara(): String {
        var emptyLineIndex = fullDescription.indexOf("\r\n\r\n")
        if (emptyLineIndex == -1) {
            emptyLineIndex = fullDescription.length
        }
        return fullDescription.substring(0, emptyLineIndex).replace("\n", " ")
    }
}