package com.ahmed.a.habib.moviecatalogapp.presentation.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ahmed.a.habib.moviecatalogapp.databinding.ItemListMoviesBinding
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.utils.BasePagingAdapter
import com.ahmed.a.habib.moviecatalogapp.utils.loadImage


class MoviesPagingAdapter : BasePagingAdapter<MovieDto, ItemListMoviesBinding>(DiffCallback()) {

    override fun getLayoutInflater(parent: ViewGroup): ItemListMoviesBinding {
        return ItemListMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ItemListMoviesBinding>, position: Int) {
        val currentItem = getItem(position)

        holder.binding.apply {
            titleTV.text = currentItem?.title
            ratingTV.text = currentItem?.voteAverage.toString()
            posterIV.loadImage(currentItem?.posterPath.orEmpty())
        }

        holder.itemView.setOnClickListener {
            listener?.invoke(it, currentItem!!, position)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MovieDto>() {
        override fun areItemsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean {
            return oldItem.movieId == newItem.movieId
        }

        override fun areContentsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean {
            return oldItem == newItem
        }
    }
}