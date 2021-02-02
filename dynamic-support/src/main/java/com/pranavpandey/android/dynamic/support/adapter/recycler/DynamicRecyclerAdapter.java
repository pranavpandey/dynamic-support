package com.pranavpandey.android.dynamic.support.adapter.recycler;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pranav Pandey
 */
public class DynamicRecyclerAdapter<V extends ViewType, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private final Object mLock = new Object();
    private final List<Binder<V, VH>> mObjects = new ArrayList<>();

    @Override
    public VH onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return getItemByViewType(viewType).onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        getItem(position).onBindViewHolder(holder, position);
    }

    @Override
    public void onViewRecycled(VH holder) {
        final int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            final Binder<V, VH> item = getItem(position);
            if (item != null) {
                item.onViewRecycled(holder);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    @Override
    public int getItemViewType(int position) {
        final Binder<V, VH> item = getItem(position);
        if (item != null) {
            return item.getViewType().viewType();
        } else {
            return RecyclerView.INVALID_TYPE;
        }
    }

    public Binder<V, VH> getItem(int position) {
        if (mObjects.size() <= position) {
            return null;
        }
        return mObjects.get(position);
    }

    private Binder<V, VH> getItemByViewType(final int viewType) {
        for (final Binder<V, VH> item : mObjects) {
            if (item.getViewType().viewType() == viewType) {
                return item;
            }
        }
        throw new IllegalStateException("binder doesn't exist in list.");
    }

    void insert(final Binder<V, VH> object, final int index) {
        synchronized (mLock) {
            mObjects.add(index, object);
        }
    }

    void remove(final Binder<V, VH> object) {
        synchronized (mLock) {
            object.onRemoved();
            mObjects.remove(object);
        }
    }

    void clear() {
        synchronized (mLock) {
            for (final Binder<V, VH> item : mObjects) {
                item.onRemoved();
            }
            mObjects.clear();
        }
    }
}
