package com.aar.app.wordsearch.mainmenu;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aar.app.wordsearch.R;
import com.aar.app.wordsearch.commons.DurationFormatter;
import com.aar.app.wordsearch.model.GameData;
import com.aar.app.wordsearch.model.GameDataInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdularis on 20/07/17.
 */

public class GameDataInfoAdapter extends ArrayAdapter<GameDataInfo> {

    private final int mResId;
    private OnDeleteItemClickListener mDeleteItemClickListener;

    public GameDataInfoAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        mResId = resource;
    }

    public void setOnDeleteItemClickListener(OnDeleteItemClickListener listener) {
        mDeleteItemClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(mResId, parent, false);
            holder = new Holder(view);
            view.setTag(holder);
        }
        else {
            holder = (Holder) view.getTag();
        }

        GameDataInfo dt = getItem(position);
        if (dt != null) {
            setHolderData(holder, dt);
        }

        return view;
    }

    private void setHolderData(Holder holder, GameDataInfo info) {
        holder.textName.setText(info.getName());
        holder.textDuration.setText(DurationFormatter.fromInteger(info.getDuration()));
        String desc = getContext().getString(R.string.game_data_desc);
        desc = desc.replaceAll(":gridSize", info.getGridRowCount() + "x" + info.getGridColCount());
        desc = desc.replaceAll(":wordCount", String.valueOf(info.getUsedWordsCount()));
        holder.textOtherDesc.setText(desc);
        if (holder.deleteItemClick == null) {
            holder.deleteItemClick = new DeleteItemClick(info);
            holder.viewDeleteItem.setOnClickListener(holder.deleteItemClick);
        } else {
            holder.deleteItemClick.info = info;
        }
    }

    class Holder {
        @BindView(R.id.text_name) TextView textName;
        @BindView(R.id.text_duration) TextView textDuration;
        @BindView(R.id.text_desc) TextView textOtherDesc;
        @BindView(R.id.delete_list_item) View viewDeleteItem;

        DeleteItemClick deleteItemClick;

        Holder(View view) {
            ButterKnife.bind(this, view);
        }


    }

    public interface OnDeleteItemClickListener {

        void onDeleteItemClick(GameDataInfo info);

    }

    private class DeleteItemClick implements View.OnClickListener {

        GameDataInfo info;

        DeleteItemClick(GameDataInfo info) {
            this.info = info;
        }

        @Override
        public void onClick(View v) {
            if (mDeleteItemClickListener != null) {
                mDeleteItemClickListener.onDeleteItemClick(info);
            }
        }
    }
}
