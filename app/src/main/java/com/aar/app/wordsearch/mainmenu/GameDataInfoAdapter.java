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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdularis on 20/07/17.
 */

public class GameDataInfoAdapter extends ArrayAdapter<GameData.Info> {

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

        GameData.Info dt = getItem(position);
        if (dt != null) {
            setHolderData(holder, dt);
        }

        return view;
    }

    private void setHolderData(Holder holder, GameData.Info info) {
        holder.textName.setText(info.getName());
        holder.textDuration.setText(DurationFormatter.fromInteger(info.getDuration()));
        if (holder.deleteItemClick == null) {
            holder.deleteItemClick = new DeleteItemClick(info);
            holder.viewDeleteItem.setOnClickListener(holder.deleteItemClick);
        } else {
            holder.deleteItemClick.info = info;
        }
    }

    class Holder {
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.text_duration)
        TextView textDuration;
        @BindView(R.id.delete_list_item)
        View viewDeleteItem;

        DeleteItemClick deleteItemClick;

        Holder(View view) {
            ButterKnife.bind(this, view);
        }


    }

    public interface OnDeleteItemClickListener {

        void onDeleteItemClick(GameData.Info info);

    }

    private class DeleteItemClick implements View.OnClickListener {

        GameData.Info info;

        DeleteItemClick(GameData.Info info) {
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
