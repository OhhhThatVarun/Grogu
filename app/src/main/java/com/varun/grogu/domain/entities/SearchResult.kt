package com.varun.grogu.domain.entities

/**
 * Class that wraps the search result in the domain layer.
 */
data class SearchResult<T>(
    val results: List<T>,
    val previous: String?,
    val next: String?
)