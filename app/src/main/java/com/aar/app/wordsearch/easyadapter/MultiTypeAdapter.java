package com.aar.app.wordsearch.easyadapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MultiTypeAdapter extends RecyclerView.Adapter {

    private SparseArray<AdapterDelegate> mDelegates;
    private List<Object> mData;

    public MultiTypeAdapter() {
        mDelegates = new SparseArray<>();
        mData = new ArrayList<>();
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

    public <T> void setItems(List<T> models) {
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

}
