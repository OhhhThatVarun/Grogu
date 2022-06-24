package com.varun.grogu.presentation.features.home

import androidx.lifecycle.*
import androidx.paging.*
import com.varun.grogu.domain.entities.Character
import com.varun.grogu.domain.repositories.StarWarsRepository
import com.varun.grogu.presentation.extensions.debounce
import com.varun.grogu.presentation.paging.CharacterSearchPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [ViewModel] to handle state of [HomeFragment]
 *
 * @param starWarsRepository: Repository used for retrieving data.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val starWarsRepository: StarWarsRepository) : ViewModel() {

    /**
     * Two-Way binding variable for user search query input.
     */
    val searchQuery: MutableLiveData<String> = MutableLiveData()

    /**
     * Exposed debounced [LiveData] with 500ms delay that emits [PagingData] of [Character].
     */
    val characters = searchQuery.debounce(coroutineScope = viewModelScope).switchMap { query ->
        if (query.isNotBlank()) {
            characterPager.liveData.cachedIn(viewModelScope) // cachedIn() shares the paging state across multiple consumers of posts,
        } else {
            MutableLiveData()
        }
    }

    private val characterPager by lazy {
        val searchPagingConfig = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false)
        Pager(config = searchPagingConfig) {
            CharacterSearchPagingSource(starWarsRepository, getCurrentValidSearchQuery())
        }
    }

    /**
     * Initiates search by re-setting the same value of [searchQuery] to itself.
     */
    fun retrySearch() {
        searchQuery.postValue(getCurrentValidSearchQuery())
    }

    /**
     * Sanitize the [searchQuery] by removing blank spaces from the start and the end
     */
    private fun getCurrentValidSearchQuery(): String {
        return searchQuery.value?.trim() ?: String()
    }
}