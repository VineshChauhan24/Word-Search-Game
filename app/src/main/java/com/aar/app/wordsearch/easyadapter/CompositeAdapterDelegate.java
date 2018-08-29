package com.aar.app.wordsearch.easyadapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aar.app.wordsearch.R;

import java.util.ArrayList;
import java.util.List;

public class CompositeAdapterDelegate extends AdapterDelegate<CompositeData, CompositeAdapterDelegate.ViewHolder> {

    private List<AdapterDelegate> mDelegates = new ArrayList<>();
    private int mLayoutRes;

    public CompositeAdapterDelegate(@LayoutRes int layoutRes) {
        super(CompositeData.class);
        mLayoutRes = layoutRes;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(mLayoutRes, parent, false));
        holder.recyclerView.setLayoutManager(onCreateLayoutManager(parent.getContext()));
        for (AdapterDelegate delegate : mDelegates) {
            holder.adapter.addDelegate(delegate);
        }
        return holder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(CompositeData model, ViewHolder holder) {
        holder.adapter.setItems(model.data);
    }

    protected RecyclerView.LayoutManager onCreateLayoutManager(@NonNull Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    public void addDelegate(AdapterDelegate delegate) {
        mDelegates.add(delegate);
    }

    public void removeDelegate(AdapterDelegate delegate) {
        mDelegates.remove(delegate);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        MultiTypeAdapter adapter;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            if (recyclerView == null) {
                throw new RuntimeException("CompositeAdapterDelegate::ViewHolder: " +
                        "Make sure item layout resource has the RecyclerView with id: recycler_view");
            }
            adapter = new MultiTypeAdapter();
            recyclerView.setAdapter(adapter);

            Log.d("RVIN", "new adapter");
        }
    }
}
