/*
 * Copyright 2018-2022 Pranav Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pranavpandey.android.dynamic.support.recyclerview.binder;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicBinderAdapter;

/**
 * A data binder class to bind data with the view inside the {@link DynamicBinderAdapter}.
 * <p>Extend this class to implement your own data binding logic.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class DynamicRecyclerViewBinder<VH extends RecyclerView.ViewHolder> {

    /**
     * The dynamic binder adapter for the recycler view.
     */
    private final DynamicBinderAdapter mBinderAdapter;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param binderAdapter The dynamic binder adapter for the recycler view.
     */
    public DynamicRecyclerViewBinder(@NonNull DynamicBinderAdapter binderAdapter) {
        mBinderAdapter = binderAdapter;
    }

    /**
     * Get the {@link RecyclerView.ViewHolder} for this data binder.
     *
     * @param parent The parent for this view holder.
     * @param viewType The type of the view to create the view holder.
     *
     * @return The view holder associated with this data binder.
     */
    public abstract @NonNull VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    /**
     * Bind {@link RecyclerView.ViewHolder} at a particular position.
     *
     * @param viewHolder The view holder for the recycler view.
     * @param position The position to bind the view holder.
     */
    public abstract void onBindViewHolder(@NonNull VH viewHolder, int position);

    /**
     * Get the recycler view adapter associated with this data binder.
     *
     * @return The recycler view adapter associated with this data binder.
     */
    public @Nullable RecyclerView.Adapter<RecyclerView.ViewHolder> getRecyclerViewAdapter() {
        return mBinderAdapter;
    }

    /**
     * Get the recycler view adapter associated with this data binder.
     *
     * @return The recycler view adapter associated with this data binder.
     */
    public @NonNull DynamicBinderAdapter getBinderAdapter() {
        return mBinderAdapter;
    }

    /**
     * Returns the total no. of items in this data binder.
     *
     * @return The total no. of items in this data binder.
     */
    public abstract int getItemCount();

    /**
     * Notify adapter when the data has been changed.
     */
    public final void notifyDataSetChanged() {
        if (!getBinderAdapter().isComputingLayout()) {
            getBinderAdapter().notifyDataSetChanged();
        }
    }

    /**
     * Notify adapter when an item has been changed in this data binder.
     *
     * @param position The position at which the item has been changed.
     */
    public final void notifyBinderItemChanged(int position) {
        if (!getBinderAdapter().isComputingLayout()) {
            getBinderAdapter().notifyBinderItemChanged(this, position);
        }
    }

    /**
     * Notify adapter when the data has been changed in this data binder.
     */
    public final void notifyBinderDataSetChanged() {
        if (!getBinderAdapter().isComputingLayout()) {
            notifyBinderItemChanged(0);
        }
    }

    /**
     * Notify adapter when an item has been inserted in this data binder.
     *
     * @param position The position at which the item has been inserted.
     */
    public final void notifyBinderItemInserted(int position) {
        if (!getBinderAdapter().isComputingLayout()) {
            getBinderAdapter().notifyBinderItemInserted(this, position);
        }
    }

    /**
     * Notify adapter when an item has been moved in this data binder.
     *
     * @param fromPosition Initial position of the moved item.
     * @param toPosition Final position of the moved item.
     */
    public final void notifyBinderItemMoved(int fromPosition, int toPosition) {
        if (!getBinderAdapter().isComputingLayout()) {
            getBinderAdapter().notifyBinderItemMoved(this, fromPosition, toPosition);
        }
    }

    /**
     * Notify adapter when an item has been removed in this data binder.
     *
     * @param position The position at which the item has been removed.
     */
    public final void notifyBinderItemRemoved(int position) {
        if (!getBinderAdapter().isComputingLayout()) {
            getBinderAdapter().notifyBinderItemRemoved(this, position);
        }
    }

    /**
     * Notify adapter when the item range of this data binder has been changed.
     *
     * @param position The position at which the first item has been changed.
     * @param itemCount Total no. of items have been changed.
     */
    public final void notifyBinderItemRangeChanged(int position, int itemCount) {
        if (!getBinderAdapter().isComputingLayout()) {
            getBinderAdapter().notifyBinderItemRangeChanged(this, position, itemCount);
        }
    }

    /**
     * Notify adapter when a set of items have been inserted in this
     * data binder.
     *
     * @param position The position at which the first item has been inserted.
     * @param itemCount Total no. of items have been inserted.
     */
    public final void notifyBinderItemRangeInserted(int position, int itemCount) {
        if (!getBinderAdapter().isComputingLayout()) {
            getBinderAdapter().notifyBinderItemRangeInserted(this, position, itemCount);
        }
    }

    /**
     * Notify adapter when a set of items have been removed in this data binder.
     *
     * @param position The position at which the first item has been removed.
     * @param itemCount Total no. of items have been removed.
     */
    public final void notifyBinderItemRangeRemoved(int position, int itemCount) {
        if (!getBinderAdapter().isComputingLayout()) {
            getBinderAdapter().notifyBinderItemRangeRemoved(this, position, itemCount);
        }
    }
}
