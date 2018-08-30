package com.aar.app.wordsearch.features.gamehistory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aar.app.wordsearch.R;
import com.aar.app.wordsearch.commons.DurationFormatter;
import com.aar.app.wordsearch.model.GameDataInfo;
import com.aar.app.wordsearch.easyadapter.AdapterDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameDataAdapterDelegate extends AdapterDelegate<GameDataInfo, GameDataAdapterDelegate.ViewHolder> {

    private OnClickListener mListener;

    public GameDataAdapterDelegate() {
        super(GameDataInfo.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_data_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GameDataInfo model, ViewHolder holder) {
        holder.textName.setText(model.getName());
        holder.textDuration.setText(DurationFormatter.fromInteger(model.getDuration()));
        String desc = holder.itemView.getContext().getString(R.string.game_data_desc);
        desc = desc.replaceAll(":gridSize", model.getGridRowCount() + "x" + model.getGridColCount());
        desc = desc.replaceAll(":wordCount", String.valueOf(model.getUsedWordsCount()));
        holder.textOtherDesc.setText(desc);

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(model);
            }
        });
        holder.viewDeleteItem.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onDeleteClick(model);
            }
        });
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.text_duration) TextView textDuration;
        @BindView(R.id.text_desc) TextView textOtherDesc;
        @BindView(R.id.delete_list_item)
        View viewDeleteItem;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener {
        void onClick(GameDataInfo gameDataInfo);
        void onDeleteClick(GameDataInfo gameDataInfo);
    }
}
