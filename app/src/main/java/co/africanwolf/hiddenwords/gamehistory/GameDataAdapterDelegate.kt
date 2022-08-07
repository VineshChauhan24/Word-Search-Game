package co.africanwolf.hiddenwords.gamehistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.africanwolf.hiddenwords.R
import co.africanwolf.hiddenwords.commons.DurationFormatter
import co.africanwolf.hiddenwords.databinding.ItemGameDataHistoryBinding
import co.africanwolf.hiddenwords.easyadapter.AdapterDelegate
import co.africanwolf.hiddenwords.model.GameDataInfo

class GameDataAdapterDelegate(var mListener: OnClickListener) : AdapterDelegate<GameDataInfo, GameDataAdapterDelegate.ViewHolder>(
    GameDataInfo::class.java
) {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val binding = ItemGameDataHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(model: GameDataInfo, holder: ViewHolder) {
        holder.bind(model)
    }

    inner class ViewHolder(private val binding: ItemGameDataHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: GameDataInfo) {

            binding.textName.text = model.name
            binding.textDuration.text = DurationFormatter.fromInteger(model.duration.toLong())
            var desc = itemView.context.getString(R.string.game_data_desc)
            desc = desc.replace(
                ":gridSize".toRegex(),
                model.gridRowCount.toString() + "x" + model.gridColCount
            )
            desc = desc.replace(":wordCount".toRegex(), model.usedWordsCount.toString())
            binding.textDesc.text = desc
            itemView.setOnClickListener {
                mListener.onClick(model)
            }
            binding.deleteListItem.setOnClickListener {
                mListener.onDeleteClick(model)
            }
        }
    }

    interface OnClickListener {
        fun onClick(gameDataInfo: GameDataInfo)
        fun onDeleteClick(gameDataInfo: GameDataInfo)
    }
}
