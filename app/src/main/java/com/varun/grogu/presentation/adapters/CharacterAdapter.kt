package com.varun.grogu.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.varun.grogu.databinding.ItemCharacterBinding
import com.varun.grogu.domain.entities.Character

private typealias CharacterClickListener = (character: Character) -> Unit

/**
 * Adapter for the [Character] list.
 *
 * @param onCharacterClick: Lambda to send click events.
 */
class CharacterAdapter(private val onCharacterClick: CharacterClickListener, private val onFavClick: (character: Character) -> Unit) : PagingDataAdapter<Character, CharacterAdapter.ViewHolder>(CharacterComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onCharacterClick, onFavClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { character -> holder.bind(character) }
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     */
    object CharacterComparator : DiffUtil.ItemCallback<Character>() {

        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * [ViewHolder] for [CharacterAdapter]
     *
     * @param binding: ViewBinding used to bind this [ViewHolder] with.
     * @param onCharacterClick: Lambda to send click events.
     */
    class ViewHolder(private val binding: ItemCharacterBinding, private val onCharacterClick: CharacterClickListener, onFavClick: (character: Character) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the [Character] with the view [binding]
         */
        fun bind(character: Character) {
            binding.character = character
            binding.root.setOnClickListener {
                onCharacterClick(character)
                // onFavClick
            }
            binding.executePendingBindings()
        }
    }
}