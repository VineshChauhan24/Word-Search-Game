package com.aar.app.wordsearch.easyadapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SimpleAdapterDelegate<T> extends AdapterDelegate<T, SimpleAdapterDelegate.ViewHolder> {

    private int mLayoutRes;
    private Binder<T> mBinder;
    private OnItemClickListener<T> mItemClickListener;

    public SimpleAdapterDelegate(@NonNull Class<T> modelClass, @LayoutRes int layoutRes,
                                 @NonNull Binder<T> binder, @Nullable OnItemClickListener<T> clickListener) {
        super(modelClass);
        mLayoutRes = layoutRes;
        mBinder = binder;
        mItemClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mLayoutRes, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(T model, ViewHolder holder) {
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) mItemClickListener.onClick(model, v);
        });
        mBinder.bind(model, holder);
    }

    public interface Binder <T> {
        void bind(T model, ViewHolder holder);
    }

    public interface OnItemClickListener <T> {
        void onClick(T model, View view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> cache = new SparseArray<>();

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T find(int resId) {
            View v = cache.get(resId);
            if (v != null) {
                return (T) v;
            }
            v = itemView.findViewById(resId);
            cache.put(resId, v);
            return (T) v;
        }
    }
}
