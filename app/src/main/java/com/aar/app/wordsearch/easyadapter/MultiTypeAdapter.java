package com.aar.app.wordsearch.easyadapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MultiTypeAdapter extends RecyclerView.Adapter {

    private SparseArray<AdapterDelegate> mDelegates;
    private List<Object> mData;
    private DiffCallback mCallback;

    public MultiTypeAdapter() {
        mDelegates = new SparseArray<>();
        mData = new ArrayList<>();
        mCallback = new DiffCallback();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterDelegate delegate = mDelegates.get(viewType);
        if (delegate != null) {
            return delegate.onCreateViewHolder(parent);
        }
        throw new RuntimeException("onCreateViewHolder: There isn't delegate for viewType " + viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterDelegate delegate = mDelegates.get(holder.getItemViewType());
        if (delegate != null) {
            delegate.onBindViewHolder(mData.get(position), holder);
        } else {
            throw new RuntimeException("onCreateViewHolder: There isn't delegate for viewType " +
                    holder.getItemViewType() + ", at position " + position);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        for (int i = 0; i < mDelegates.size(); i++) {
            AdapterDelegate delegate = mDelegates.valueAt(i);
            if (delegate.canHandleModel(mData.get(position))) {
                return delegate.getViewType();
            }
        }
        return 0;
    }

    public <T> void add(@NonNull T model) {
        mData.add(model);
    }

    public <T> void addAll(List<T> models) {
        mData.addAll(models);
    }

    public <T> void insertAt(int index, T model) {
        mData.add(index, model);
    }

    public <T> void replaceAt(int index, T model) {
        if (mData.size() > 0) {
            mData.set(index, model);
        } else {
            mData.add(index, model);
        }
    }

    public void removeAt(int index) {
        if (mData.size() > 0)
            mData.remove(index);
    }

    public <T> void setItems(List<T> models) {
        mCallback.set(mData, (List<Object>) models);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(mCallback);
        result.dispatchUpdatesTo(this);
        mData.clear();
        mData.addAll(models);
    }

    public void clear() {
        mData.clear();
    }

    public void addDelegate(AdapterDelegate delegate) {
        mDelegates.put(delegate.getViewType(), delegate);
    }

    public <T> void addDelegate(@NonNull Class<T> modelClass,
                                @LayoutRes int layoutRes,
                                @NonNull SimpleAdapterDelegate.Binder<T> binder,
                                @Nullable SimpleAdapterDelegate.OnItemClickListener<T> clickListener) {
        AdapterDelegate delegate = new SimpleAdapterDelegate<>(modelClass, layoutRes, binder, clickListener);
        mDelegates.put(delegate.getViewType(), delegate);
    }

    public void removeDelegate(AdapterDelegate delegate) {
        mDelegates.removeAt(mDelegates.indexOfValue(delegate));
    }

    public static class DiffCallback extends DiffUtil.Callback {

        List<Object> oldList;
        List<Object> newList;

        public DiffCallback() {
        }

        public void set(List<Object> oldList, List<Object> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }
    }
}
