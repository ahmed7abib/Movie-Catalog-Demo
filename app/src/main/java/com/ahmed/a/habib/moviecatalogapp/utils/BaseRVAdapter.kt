package com.ahmed.a.habib.moviecatalogapp.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRVAdapter<T : Any, VB : ViewBinding>(callback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, BaseRVAdapter.BaseViewHolder<VB>>(callback) {

    abstract fun getLayoutInflater(parent: ViewGroup): VB

    var listener: ((view: View, item: T, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder<VB>(
        getLayoutInflater(parent)
    )

    class BaseViewHolder<VB : ViewBinding>(val binding: VB) :
        RecyclerView.ViewHolder(binding.root)
}
