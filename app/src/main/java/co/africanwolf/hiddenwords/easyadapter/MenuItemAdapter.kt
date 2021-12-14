package co.africanwolf.hiddenwords.easyadapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.africanwolf.hiddenwords.data.entity.Game
import co.africanwolf.hiddenwords.databinding.ViewMenuItemBinding

class MenuItemAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Game, MenuItemAdapter.MyViewHolder>(MyDiffUtil) {

    companion object MyDiffUtil : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class MyViewHolder(private val binding: ViewMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Game) {
            binding.category.text = item.type.type
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
        fun onClick(game: Game)
    }
}
