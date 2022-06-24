package com.varun.grogu.presentation.features.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.varun.grogu.R
import com.varun.grogu.databinding.FragmentDetailBinding
import com.varun.grogu.domain.entities.Film
import com.varun.grogu.domain.utils.Resource
import com.varun.grogu.presentation.adapters.FilmAdapter
import com.varun.grogu.presentation.extensions.getDataBinding
import com.varun.grogu.presentation.extensions.showSnackBar
import com.varun.grogu.presentation.extensions.showSnackBarWithRetry
import dagger.hilt.android.AndroidEntryPoint
import logcat.LogPriority
import logcat.asLog
import logcat.logcat

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel by viewModels<DetailViewModel>()
    private val args: DetailFragmentArgs by navArgs()
    private val filmAdapter = FilmAdapter(::onFilmClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadHomePlanet()
        loadFilms()
        loadSpecies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = getDataBinding(R.layout.fragment_detail, container)
        return binding.apply {
            character = args.character
            species = null
            filmList.layoutManager = GridLayoutManager(context, 2)
            adapter = this@DetailFragment.filmAdapter
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.species.observe(viewLifecycleOwner) { speciesResource ->
            when (speciesResource) {
                is Resource.Loading -> {
                    binding.speciesProgressBar.isVisible = true
                    return@observe
                }
                is Resource.Success -> {
                    binding.species = speciesResource.data
                }
                is Resource.Failure -> {
                    showSnackBarWithRetry(R.string.error_loading_species) {
                        loadHomePlanet()
                    }
                    logcat(LogPriority.ERROR) { speciesResource.throwable.asLog() }
                }
            }
            binding.speciesProgressBar.isVisible = false
        }
        viewModel.planet.observe(viewLifecycleOwner) { planetResource ->
            when (planetResource) {
                is Resource.Loading -> {
                    binding.planetProgressBar.isVisible = true
                    return@observe
                }
                is Resource.Success -> {
                    binding.planet = planetResource.data
                }
                is Resource.Failure -> {
                    showSnackBarWithRetry(R.string.error_loading_planet) {
                        loadHomePlanet()
                    }
                    logcat(LogPriority.ERROR) { planetResource.throwable.asLog() }
                }
            }
            binding.planetProgressBar.isVisible = false
        }
        viewModel.films.observe(viewLifecycleOwner) { filmsResources ->
            var failedFilmCounter = 0
            val films = filmsResources.mapNotNull { filmResources ->
                when (filmResources) {
                    is Resource.Loading -> {
                        null
                    }
                    is Resource.Success -> {
                        filmResources.data
                    }
                    is Resource.Failure -> {
                        failedFilmCounter++
                        logcat(LogPriority.ERROR) { filmResources.throwable.asLog() }
                        null
                    }
                }
            }
            if (films.isEmpty() && failedFilmCounter != 0) {
                showSnackBarWithRetry(R.string.unable_to_load_films) {
                    loadFilms()
                }
            } else {
                if (failedFilmCounter != 0) {
                    showSnackBar(getString(R.string.unable_to_load_n_films, failedFilmCounter))
                }
                filmAdapter.setFilms(films)
            }
            binding.moviesProgressBar.isVisible = false
        }
    }

    private fun loadFilms() {
        viewModel.loadFilms(args.character)
    }

    private fun loadHomePlanet() {
        viewModel.loadHomePlanet(args.character)
    }

    private fun loadSpecies() {
        viewModel.loadSpecies(args.character)
    }

    /**
     * Navigates to the [FilmDetailFragment]
     */
    private fun onFilmClick(film: Film) {
        findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToFilmDetailFragment(film))
    }
}