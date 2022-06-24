package com.varun.grogu.presentation.features.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.varun.grogu.R
import com.varun.grogu.databinding.FragmentFilmDetailBinding
import com.varun.grogu.presentation.extensions.getDataBinding

class FilmDetailFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilmDetailBinding
    private val args: FilmDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = getDataBinding(R.layout.fragment_film_detail, container)
        return binding.apply {
            film = args.film
        }.root
    }
}