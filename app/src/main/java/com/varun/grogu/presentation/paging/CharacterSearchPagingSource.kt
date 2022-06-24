package com.varun.grogu.presentation.paging

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.varun.grogu.domain.entities.Character
import com.varun.grogu.domain.entities.SearchResult
import com.varun.grogu.domain.repositories.StarWarsRepository
import kotlinx.coroutines.withContext

/**
 * A [PagingSource] that uses the next/previous field of [SearchResult] as the key for next/prev pages.
 *
 * @param starWarsRepository: Repository used for retrieving data.
 * @param query: String used to make the search
 */
class CharacterSearchPagingSource(private val starWarsRepository: StarWarsRepository, private val query: String) : PagingSource<Int, Character>() {

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return withContext(starWarsRepository.getCoroutineDispatcher()) {
            try {
                val result = starWarsRepository.searchCharacters(query, params.key)
                val nextPage = getPageNumber(result.next)
                val prevPage = getPageNumber(result.previous)
                LoadResult.Page(result.results, prevPage, nextPage)
            } catch (throwable: Throwable) {
                LoadResult.Error(throwable)
            }
        }
    }

    /**
     * Extracts the value of page query parameter from a url and returns it.
     *
     * e.g. If given https://swapi.dev/api/people/?search=a&page=2 the function will return 2
     * e.g. If given https://swapi.dev/api/people/?search=a or null the function will return null
     */
    private fun getPageNumber(url: String?): Int? {
        return try {
            Uri.parse(url)?.getQueryParameter("page")?.toIntOrNull()
        } catch (e: Exception) {
            null
        }
    }
}