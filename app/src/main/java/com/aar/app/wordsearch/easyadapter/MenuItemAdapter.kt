package com.aar.app.wordsearch.easyadapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aar.app.wordsearch.data.entity.MenuItem
import com.aar.app.wordsearch.databinding.ViewMenuItemBinding

class MenuItemAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<MenuItem, MenuItemAdapter.MyViewHolder>(MyDiffUtil) {

    companion object MyDiffUtil : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class MyViewHolder(private val binding: ViewMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuItem) {
            binding.category.text = item.name
            binding.container.setBackgroundColor(Color.parseColor(item.color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ViewMenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val meme = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(meme)
        }
        holder.bind(meme)
    }

    interface OnClickListener {
        fun onClick(menuItem: MenuItem)
    }
}
