package com.varun.grogu.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varun.grogu.databinding.ItemFilmBinding
import com.varun.grogu.domain.entities.Film

private typealias FilmClickListener = (film: Film) -> Unit

/**
 * Adapter for the [Film] list.
 *
 * @param onFilmClick: Lambda to send click events.
 */
class FilmAdapter(private val onFilmClick: FilmClickListener) : RecyclerView.Adapter<FilmAdapter.ViewHolder>() {

    /**
     * Data holder for this adapter
     */
    private val films = mutableListOf<Film>()

    @SuppressLint("NotifyDataSetChanged")
    fun setFilms(films: List<Film>?) {
        if (films != null) {
            this.films.clear()
            this.films.addAll(films)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onFilmClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(films[position])
    }

    override fun getItemCount(): Int {
        return films.size
    }

    /**
     * [ViewHolder] for [FilmAdapter]
     *
     * @param binding: ViewBinding used to bind this [ViewHolder] with.
     * @param onFilmClick: Lambda to send click events.
     */
    class ViewHolder(private val binding: ItemFilmBinding, private val onFilmClick: FilmClickListener) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the [Film] with the view [binding]
         */
        fun bind(film: Film) {
            binding.film = film
            binding.root.setOnClickListener {
                onFilmClick(film)
            }
            binding.executePendingBindings()
        }
    }
}
