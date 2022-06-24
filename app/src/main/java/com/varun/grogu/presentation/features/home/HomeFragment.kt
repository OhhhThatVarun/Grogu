package com.varun.grogu.presentation.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.facebook.shimmer.ShimmerFrameLayout
import com.varun.grogu.R
import com.varun.grogu.databinding.FragmentHomeBinding
import com.varun.grogu.domain.entities.Character
import com.varun.grogu.presentation.adapters.CharacterAdapter
import com.varun.grogu.presentation.extensions.*
import com.varun.grogu.presentation.features.detail.FilmDetailFragment
import com.varun.grogu.presentation.paging.CharacterSearchPagingSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.logcat

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val adapter = CharacterAdapter(::onCharacterClicked, ::onAddFav)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = getDataBinding(R.layout.fragment_home, container)
        return binding.apply {
            viewModel = this@HomeFragment.viewModel
            adapter = this@HomeFragment.adapter
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Observe the [Character] paging data and submit it's results to [CharacterAdapter]
         */
        viewModel.characters.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
        /**
         * Collect all the state in which the [CharacterSearchPagingSource] is going through
         */
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        return@collectLatest startShimmer(view)
                    }
                    is LoadState.NotLoading -> {
                        if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                            showSnackBar(R.string.no_results_found)
                        }
                    }
                    is LoadState.Error -> {
                        showSnackBarWithRetry(R.string.some_error_occurred) {
                            viewModel.retrySearch()
                        }
                        logcat(LogPriority.ERROR) {
                            (loadState.refresh as LoadState.Error).error.localizedMessage ?: "Unknown error"
                        }
                    }
                }
                stopShimmer(view)
            }
        }
    }

    /**
     * Starts the Shimmer effect on the RecyclerView
     */
    private fun startShimmer(view: View) {
        view.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout).showAndStartEffect()
    }

    /**
     * Stops the Shimmer effect on the RecyclerView
     */
    private fun stopShimmer(view: View) {
        view.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout).hideAndStopEffect()
    }

    /**
     * Navigates to the [FilmDetailFragment]
     */
    private fun onCharacterClicked(character: Character) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(character))
    }

    private fun onAddFav(character: Character) {

    }
}