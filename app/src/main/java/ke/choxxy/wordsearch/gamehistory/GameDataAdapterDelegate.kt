package ke.choxxy.wordsearch.gamehistory

import ke.choxxy.wordsearch.easyadapter.AdapterDelegate
import ke.choxxy.wordsearch.model.GameDataInfo
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import ke.choxxy.wordsearch.R
import ke.choxxy.wordsearch.commons.DurationFormatter
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import android.widget.TextView
import butterknife.ButterKnife

class GameDataAdapterDelegate : AdapterDelegate<GameDataInfo, GameDataAdapterDelegate.ViewHolder>(
    GameDataInfo::class.java
) {
    private var mListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game_data_history, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(model: GameDataInfo, holder: ViewHolder) {
        holder.textName!!.text = model.name
        holder.textDuration!!.text = DurationFormatter.fromInteger(model.duration.toLong())
        var desc = holder.itemView.context.getString(R.string.game_data_desc)
        desc = desc.replace(
            ":gridSize".toRegex(),
            model.gridRowCount.toString() + "x" + model.gridColCount
        )
        desc = desc.replace(":wordCount".toRegex(), model.usedWordsCount.toString())
        holder.textOtherDesc!!.text = desc
        holder.itemView.setOnClickListener { v: View? ->
            if (mListener != null) {
                mListener!!.onClick(model)
            }
        }
        holder.viewDeleteItem!!.setOnClickListener { v: View? ->
            if (mListener != null) {
                mListener!!.onDeleteClick(model)
            }
        }
    }

    fun setOnClickListener(listener: OnClickListener?) {
        mListener = listener
    }

    class ViewHolder internal constructor(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        @BindView(R.id.text_name)
        var textName: TextView? = null

        @BindView(R.id.text_duration)
        var textDuration: TextView? = null

        @BindView(R.id.text_desc)
        var textOtherDesc: TextView? = null

        @BindView(R.id.delete_list_item)
        var viewDeleteItem: View? = null

        init {
            ButterKnife.bind(this, itemView!!)
        }
    }

    interface OnClickListener {
        fun onClick(gameDataInfo: GameDataInfo)
        fun onDeleteClick(gameDataInfo: GameDataInfo)
    }
}